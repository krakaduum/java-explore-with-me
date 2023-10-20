package ru.practicum.service.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.enums.EventLifecycle;
import ru.practicum.model.enums.PublishEventStatus;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.service.admin_service.AdminEventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public Collection<EventFullDto> getEvents(Collection<Long> users,
                                              Collection<String> states,
                                              Collection<Long> categories,
                                              LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd,
                                              Integer from,
                                              Integer size) {
        return eventRepository
                .findEventsForAdmin(users, states, categories, rangeStart, rangeEnd, Pageable.ofSize(from + size))
                .stream()
                .skip(from)
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest request) {
        Event event = eventRepository.findById(eventId).get();

        if (request.getEventDate() != null) {
            LocalDateTime newEventDate = LocalDateTime.parse(request.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (newEventDate.isBefore(LocalDateTime.now())) {
                throw new InvalidDateException("Новая дата начала должна быть в будущем");
            }

            LocalDateTime publishDate = event.getPublishedOn();

            if (newEventDate.isBefore(publishDate.minusHours(1))) {
                throw new InvalidDateException("Дата начала изменяемого события должна быть не ранее чем за час " +
                        "от даты публикации");
            }
        }

        if (request.getStateAction() != null) {
            if (EventLifecycle.valueOf(event.getState()) != EventLifecycle.PENDING) {
                throw new InvalidParamException("Неверное состояние события " + event.getState());
            }

            if (request.getStateAction() == PublishEventStatus.PUBLISH_EVENT) {
                event.setState(EventLifecycle.PUBLISHED.toString());
                event.setPublishedOn(LocalDateTime.now());
            } else {
                event.setState(EventLifecycle.CANCELED.toString());
            }
        }

        if (request.getLocation() != null) {
            Location location = locationRepository.save(request.getLocation());
            event.setLocation(location);
        }

        if (request.getAnnotation() != null && !request.getAnnotation().trim().isEmpty()) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            Optional<Category> category = categoryRepository.findById(request.getCategory());

            event.setCategory(category.get());
        }

        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(request.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            event.setTitle(request.getTitle());
        }

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }
}
