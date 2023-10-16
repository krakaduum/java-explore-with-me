package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {

    Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean unique);

    void saveHit(EndpointHitDto endpointHitDto);

}
