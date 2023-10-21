package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.constant.Constants;
import ru.practicum.model.Location;
import ru.practicum.model.enums.PublishEventStatus;

import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = Constants.DateTimePattern)
    String eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    PublishEventStatus stateAction;

    @Size(min = 3, max = 120)
    String title;
}
