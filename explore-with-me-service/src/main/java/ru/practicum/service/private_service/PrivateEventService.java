package ru.practicum.service.private_service;

import ru.practicum.dto.*;

import java.util.Collection;

public interface PrivateEventService {
    Collection<EventShortDto> getEvents(long userId, Integer from, Integer size);

    EventFullDto addEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateRequest);

    Collection<ParticipationRequestDto> getEventParticipants(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest updateRequest);
}
