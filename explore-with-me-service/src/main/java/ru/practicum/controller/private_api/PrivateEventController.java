package ru.practicum.controller.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.*;
import ru.practicum.service.private_service.PrivateEventService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping(path = "/{userId}/events")
    Collection<EventShortDto> getEvents(@PathVariable Long userId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return privateEventService.getEvents(userId, from, size);
    }

    @PostMapping(path = "/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        return privateEventService.addEvent(userId, newEventDto);
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/events/{eventId}")
    EventFullDto updateEvent(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @RequestBody @Valid UpdateEventUserRequest updateRequest) {
        return privateEventService.updateEvent(userId, eventId, updateRequest);
    }

    @GetMapping(path = "/{userId}/events/{eventId}/requests")
    Collection<ParticipationRequestDto> getEventParticipants(@PathVariable Long userId, @PathVariable Long eventId) {
        return privateEventService.getEventParticipants(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult changeRequestStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        return privateEventService.changeRequestStatus(userId, eventId, updateRequest);
    }
}
