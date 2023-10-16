package ru.practicum.stats.server.service;

import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.mapper.EndpointHitMapper;
import ru.practicum.stats.server.mapper.ViewStatsMapper;
import ru.practicum.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public Collection<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end,
                                             Collection<String> uris, boolean unique) {
        if (uris == null) {
            if (unique) {
                return statsRepository.findUniqueStats(start, end)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findAllStats(start, end)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            }
        } else {
            if (unique) {
                return statsRepository.findUniqueStatsWithUri(start, end, uris)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findAllStatsWithUri(start, end, uris)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            }
        }
    }

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

}
