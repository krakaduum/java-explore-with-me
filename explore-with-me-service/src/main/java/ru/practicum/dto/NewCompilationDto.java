package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewCompilationDto {
    List<Long> events = new ArrayList<>();

    boolean pinned;

    @NotBlank
    @Size(min = 1, max = 50)
    String title;
}
