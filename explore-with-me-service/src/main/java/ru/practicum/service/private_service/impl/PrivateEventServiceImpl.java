package ru.practicum.service.private_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constant.Constants;
import ru.practicum.dto.*;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.EventRequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.EventRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventLifecycle;
import ru.practicum.model.enums.EventRequestStatus;
import ru.practicum.model.enums.ReviewEventStatus;
import ru.practicum.model.enums.UpdateEventStatus;
import ru.practicum.repository.*;
import ru.practicum.service.private_service.PrivateEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public Collection<EventShortDto> getEvents(long userId, Integer from, Integer size) {
        if ((from != null && from < 0) || (size != null && size <= 0)) {
            throw new IllegalArgumentException("Неверные параметры поиска");
        }

        if (from == null) {
            from = 0;
        }

        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        return eventRepository
                .findEventsByInitiatorId(userId, Pageable.ofSize(from + size))
                .stream()
                .skip(from)
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto addEvent(long userId, NewEventDto newEventDto) {
        Optional<User> user = userRepository.findById(userId);

        LocalDateTime eventDate = newEventDto.getEventDate();

        if (eventDate.isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new InvalidDateException("Дата и время, на которые намечено событие, не могут быть раньше, чем " +
                    "через два часа от чекущего момента");
        }

        Optional<Category> category = categoryRepository.findById(newEventDto.getCategory());

        locationRepository.save(newEventDto.getLocation());

        Event newEvent = new Event(null,
                newEventDto.getAnnotation(),
                category.get(),
                0L,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                eventDate,
                user.get(),
                newEventDto.getLocation(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                LocalDateTime.now(),
                newEventDto.getRequestModeration(),
                EventStatus.PENDING.toString(),
                newEventDto.getTitle(),
                0L);

        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    @Override
    public EventFullDto getEvent(long userId, long eventId) {
        return EventMapper.toEventFullDto(eventRepository.findByInitiatorIdAndId(userId, eventId));
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateRequest) {
        if (updateRequest.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime
                    .parse(updateRequest.getEventDate(), DateTimeFormatter.ofPattern(Constants.DateTimePattern));

            if (eventDate.isBefore(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
                throw new InvalidDateException("Дата и время, на которые намечено событие, не могут быть раньше, чем " +
                        "через два часа от чекущего момента");
            }
        }

        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            throw new NoSuchElementException("События с идентификатором " + eventId + " не существует");
        }

        EventStatus eventStatus = EventStatus.valueOf(event.get().getState());
        if (eventStatus != EventStatus.PENDING && eventStatus != EventStatus.CANCELED) {
            throw new InvalidParamException("Изменить можно только отмененные события или события в состоянии " +
                    "ожидания модерации");
        }

        if (updateRequest.getStateAction() != null) {
            if (updateRequest.getStateAction() == ReviewEventStatus.CANCEL_REVIEW) {
                event.get().setState(EventLifecycle.CANCELED.toString());
            } else {
                event.get().setState(EventLifecycle.PENDING.toString());
            }
        }

        if (updateRequest.getAnnotation() != null && !updateRequest.getAnnotation().trim().isEmpty()) {
            event.get().setAnnotation(updateRequest.getAnnotation());
        }

        if (updateRequest.getCategory() != null) {
            Optional<Category> category = categoryRepository.findById(updateRequest.getCategory());

            event.get().setCategory(category.get());
        }

        if (updateRequest.getDescription() != null && !updateRequest.getDescription().trim().isEmpty()) {
            event.get().setDescription(updateRequest.getDescription());
        }

        if (updateRequest.getEventDate() != null && !updateRequest.getEventDate().trim().isEmpty()) {
            event.get().setEventDate(LocalDateTime.parse(updateRequest.getEventDate()));
        }

        if (updateRequest.getLocation() != null) {
            event.get().setLocation(updateRequest.getLocation());
        }

        if (updateRequest.getPaid() != null) {
            event.get().setPaid(updateRequest.getPaid());
        }

        if (updateRequest.getParticipantLimit() != null) {
            event.get().setParticipantLimit(updateRequest.getParticipantLimit());
        }

        if (updateRequest.getRequestModeration() != null) {
            event.get().setRequestModeration(updateRequest.getRequestModeration());
        }

        if (updateRequest.getTitle() != null && !updateRequest.getTitle().trim().isEmpty()) {
            event.get().setTitle(updateRequest.getTitle());
        }

        return EventMapper.toEventFullDto(eventRepository.save(event.get()));
    }

    @Override
    public Collection<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId) {
        return eventRequestRepository
                .findAllByEventId(eventId)
                .stream()
                .filter(request -> request.getEvent().getInitiator().getId().equals(userId))
                .map(EventRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {
        Optional<Event> event = eventRepository.findById(eventId);

        List<Optional<EventRequest>> requests = updateRequest
                .getRequestIds()
                .stream()
                .map(eventRequestRepository::findById)
                .collect(Collectors.toList());

        for (Optional<EventRequest> request : requests) {
            if (EventRequestStatus.valueOf(request.get().getStatus()) != EventRequestStatus.PENDING) {
                throw new InvalidParamException("Статус можно изменить только у заявок, находящихся в состоянии " +
                        "ожидания");
            }
        }

        Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (updateRequest.getStatus() == UpdateEventStatus.REJECTED) {
            rejectedRequests = requests
                    .stream()
                    .map(request -> {
                        request.get().setStatus(UpdateEventStatus.REJECTED.toString());
                        eventRequestRepository.save(request.get());
                        return EventRequestMapper.toParticipationRequestDto(request.get());
                    })
                    .collect(Collectors.toList());
        }

        if (updateRequest.getStatus() == UpdateEventStatus.CONFIRMED) {
            if (event.get().getParticipantLimit() == 0 || !event.get().getRequestModeration()) {
                confirmedRequests = requests
                        .stream()
                        .map(request -> {
                            request.get().setStatus(UpdateEventStatus.CONFIRMED.toString());
                            eventRequestRepository.save(request.get());
                            return EventRequestMapper.toParticipationRequestDto(request.get());
                        })
                        .collect(Collectors.toList());
            } else if (event.get().getConfirmedRequests() >= event.get().getParticipantLimit()) {
                throw new InvalidParamException("Нельзя подтвердить заявку, если уже достигнут лимит по заявкам на " +
                        "данное событие");
            }

            for (Optional<EventRequest> request : requests) {
                if (event.get().getConfirmedRequests() < event.get().getParticipantLimit()) {
                    request.get().setStatus(UpdateEventStatus.CONFIRMED.toString());
                    confirmedRequests.add(EventRequestMapper.toParticipationRequestDto(request.get()));
                } else {
                    request.get().setStatus(UpdateEventStatus.REJECTED.toString());
                    rejectedRequests.add(EventRequestMapper.toParticipationRequestDto(request.get()));
                }
                eventRequestRepository.save(request.get());
                event.get().setConfirmedRequests(event.get().getConfirmedRequests() + 1);
            }
        }

        eventRepository.save(event.get());

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
