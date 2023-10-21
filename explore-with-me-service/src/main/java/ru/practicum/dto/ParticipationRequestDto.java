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
public class ParticipationRequestDto {
    long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DateTimePattern)
    LocalDateTime created;

    long event;

    long requester;

    String status;
}
