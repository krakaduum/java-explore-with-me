package ru.practicum.stats.server.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.ViewStats;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }

}
