package ru.practicum.controller.admin_api;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequest;
import ru.practicum.service.admin_service.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<EventFullDto> getEvents(@RequestParam(required = false) Collection<Long> users,
                                       @RequestParam(required = false) Collection<String> states,
                                       @RequestParam(required = false) Collection<Long> categories,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime rangeStart,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                       LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        return adminEventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    EventFullDto updateEvent(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest request) {
        return adminEventService.updateEvent(eventId, request);
    }
}
