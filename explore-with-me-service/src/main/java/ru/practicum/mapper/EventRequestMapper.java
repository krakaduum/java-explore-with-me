package ru.practicum.mapper;

import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.model.EventRequest;

public class EventRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(EventRequest eventRequest) {
        return new ParticipationRequestDto(
                eventRequest.getId(),
                eventRequest.getCreated(),
                eventRequest.getEvent().getId(),
                eventRequest.getRequester().getId(),
                eventRequest.getStatus());
    }
}
