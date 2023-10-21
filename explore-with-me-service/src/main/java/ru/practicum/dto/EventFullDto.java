package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.constant.Constants;
import ru.practicum.model.Location;
import ru.practicum.model.enums.EventLifecycle;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;

    String annotation;

    CategoryDto category;

    Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime createdOn;

    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime eventDate;

    UserShortDto initiator;

    Location location;

    boolean paid;

    Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime publishedOn;

    boolean requestModeration;

    EventLifecycle state;

    String title;

    Long views;
}
