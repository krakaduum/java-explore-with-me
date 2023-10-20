package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@Data
public class CompilationDto {
    long id;

    Set<EventShortDto> events;

    Boolean pinned;

    @Size(max = 50)
    String title;
}
