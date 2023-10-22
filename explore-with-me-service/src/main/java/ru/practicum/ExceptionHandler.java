package ru.practicum;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.constant.Constants;
import ru.practicum.dto.ApiError;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.exception.InvalidParamException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DateTimePattern);

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return new ApiError(errors,
                e.getMessage(),
                "400 BAR REQUEST",
                LocalDateTime.now().format(dateTimeFormatter),
                HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidParamException(InvalidDateException e) {
        return new ApiError(null,
                e.getMessage(),
                "400 BAD REQUEST",
                LocalDateTime.now().format(dateTimeFormatter),
                HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoSuchElementException(NoSuchElementException e) {
        return new ApiError(null,
                e.getMessage(),
                "404 NOT FOUND",
                LocalDateTime.now().format(dateTimeFormatter),
                HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InvalidParamException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInvalidParamException(InvalidParamException e) {
        return new ApiError(null,
                e.getMessage(),
                "409 CONFLICT",
                LocalDateTime.now().format(dateTimeFormatter),
                HttpStatus.CONFLICT);
    }
}
