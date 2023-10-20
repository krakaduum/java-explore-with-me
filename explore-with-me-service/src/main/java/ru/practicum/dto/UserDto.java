package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    long id;

    String name;

    String email;
}
