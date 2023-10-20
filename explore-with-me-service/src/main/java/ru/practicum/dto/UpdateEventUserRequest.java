package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.model.Location;
import ru.practicum.model.enums.ReviewEventStatus;

import javax.validation.constraints.Size;

@Data
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    ReviewEventStatus stateAction;

    @Size(min = 3, max = 120)
    String title;
}
