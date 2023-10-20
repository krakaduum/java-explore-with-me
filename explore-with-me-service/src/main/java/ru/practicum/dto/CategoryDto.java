package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
public class CategoryDto {
    long id;

    @Size(min = 1, max = 50)
    String name;
}
