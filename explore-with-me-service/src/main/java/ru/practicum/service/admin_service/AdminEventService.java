package ru.practicum.service.admin_service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.Collection;

public interface AdminEventService {
    Collection<EventFullDto> getEvents(Collection<Long> users,
                                       Collection<String> states,
                                       Collection<Long> categories,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Integer from,
                                       Integer size);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest request);
}
