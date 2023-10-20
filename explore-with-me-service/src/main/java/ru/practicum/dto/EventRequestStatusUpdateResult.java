package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@AllArgsConstructor
@Data
public class EventRequestStatusUpdateResult {
    Collection<ParticipationRequestDto> confirmedRequests;

    Collection<ParticipationRequestDto> rejectedRequests;
}
