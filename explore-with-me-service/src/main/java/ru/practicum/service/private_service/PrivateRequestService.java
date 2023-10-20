package ru.practicum.service.private_service;

import ru.practicum.dto.ParticipationRequestDto;

import java.util.Collection;

public interface PrivateRequestService {
    Collection<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto addParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
