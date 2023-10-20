package ru.practicum.service.private_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.EventRequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.EventRequest;
import ru.practicum.model.User;
import ru.practicum.model.enums.EventLifecycle;
import ru.practicum.model.enums.EventRequestStatus;
import ru.practicum.model.enums.UpdateEventStatus;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.EventRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.private_service.PrivateRequestService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ParticipationRequestDto> getUserRequests(long userId) {
        return eventRequestRepository.findAllByRequesterId(userId)
                .stream()
                .map(EventRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto addParticipationRequest(long userId, long eventId) {
        Event event = eventRepository.findById(eventId).get();
        boolean isUserRequestedEvent = !eventRequestRepository
                .findAllByRequesterIdAndEventId(userId, eventId)
                .isEmpty();

        if (!event.getState().equals(EventLifecycle.PUBLISHED.toString())) {
            throw new InvalidParamException("Событие должно быть опубликовано");
        }

        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new InvalidParamException("Достигнуто ограничение запросов");
            }
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new InvalidParamException("Невозможно создать запрос");
        }

        if (isUserRequestedEvent) {
            throw new InvalidParamException("Невозможно создать запрос");
        }

        User user = userRepository.findById(userId).get();

        EventRequest request = new EventRequest(LocalDateTime.now(),
                event,
                user,
                EventRequestStatus.PENDING.toString());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(UpdateEventStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return EventRequestMapper.toParticipationRequestDto(
                eventRequestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        EventRequest eventRequest = eventRequestRepository.findById(requestId).get();
        eventRequest.setStatus(EventRequestStatus.CANCELED.toString());

        return EventRequestMapper.toParticipationRequestDto(
                eventRequestRepository.save(eventRequest));
    }
}
