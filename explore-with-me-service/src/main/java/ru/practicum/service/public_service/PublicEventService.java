package ru.practicum.service.public_service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

public interface PublicEventService {
    Collection<EventShortDto> getEvents(String text,
                                        Collection<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sortBy,
                                        Integer from,
                                        Integer size,
                                        HttpServletRequest request);

    EventFullDto getEvent(long id, HttpServletRequest request);
}
