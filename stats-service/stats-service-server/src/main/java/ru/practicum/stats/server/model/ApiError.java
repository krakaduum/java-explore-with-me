package ru.practicum.stats.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@AllArgsConstructor
@Data
public class ApiError {
    List<String> errors;

    String message;

    String reason;

    String timestamp;

    HttpStatus status;
}
