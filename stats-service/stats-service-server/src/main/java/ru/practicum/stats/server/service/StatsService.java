package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.util.Collection;
import java.util.List;

public interface StatsService {

    Collection<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

    void saveHit(EndpointHitDto endpointHitDto);

}
