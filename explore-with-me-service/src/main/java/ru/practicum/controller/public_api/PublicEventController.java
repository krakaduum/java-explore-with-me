package ru.practicum.controller.public_api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constant.Constants;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.service.public_service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final PublicEventService publicEventService;

    @GetMapping
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Collection<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DateTimePattern)
                                               LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = Constants.DateTimePattern)
                                               LocalDateTime rangeEnd,
                                               @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sortBy,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        return publicEventService
                .getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sortBy, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable long id, HttpServletRequest request) {
        return publicEventService.getEvent(id, request);
    }
}
