package ru.practicum.stats.server;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.server.exception.InvalidDateException;
import ru.practicum.stats.server.model.ApiError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ExceptionHandler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidParamException(InvalidDateException e) {
        return new ApiError(null,
                e.getMessage(),
                "400 BAD REQUEST",
                LocalDateTime.now().format(dateTimeFormatter).toString(),
                HttpStatus.BAD_REQUEST);
    }
}
