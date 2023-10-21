package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.constant.Constants;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    long id;

    String annotation;

    CategoryDto category;

    Long confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime eventDate;

    UserShortDto initiator;

    boolean paid;

    String title;

    long views;
}
