package ru.practicum.controller.private_api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.service.private_service.PrivateRequestService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    @GetMapping(path = "/{userId}/requests")
    Collection<ParticipationRequestDto> getUserRequests(@PathVariable long userId) {
        return privateRequestService.getUserRequests(userId);
    }

    @PostMapping(path = "/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto createRequest(@PathVariable long userId, @RequestParam long eventId) {
        return privateRequestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/requests/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        return privateRequestService.cancelRequest(userId, requestId);
    }
}
