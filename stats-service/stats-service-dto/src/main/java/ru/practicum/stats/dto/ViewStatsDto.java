package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ViewStatsDto {

    @NotBlank
    String app;

    @NotBlank
    String uri;

    @NotNull
    Long hits;

}
