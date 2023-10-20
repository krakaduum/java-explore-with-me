package ru.practicum.dto;

import lombok.Data;
import ru.practicum.model.enums.UpdateEventStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;

    UpdateEventStatus status;
}
