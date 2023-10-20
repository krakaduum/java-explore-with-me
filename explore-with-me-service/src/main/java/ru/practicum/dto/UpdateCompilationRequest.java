package ru.practicum.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateCompilationRequest {
    List<Long> events = new ArrayList<>();

    boolean pinned;

    @Size(min = 1, max = 50)
    String title;
}
