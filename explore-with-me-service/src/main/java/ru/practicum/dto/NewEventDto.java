package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.constant.Constants;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    @NotBlank
    String annotation;

    long category;

    @NotBlank
    @Size(min = 20, max = 7000)
    @NotBlank
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime eventDate;

    Location location;

    Boolean paid = false;

    Integer participantLimit = 0;

    Boolean requestModeration = true;

    @NotBlank
    @Size(min = 3, max = 120)
    @NotBlank
    String title;
}
