package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.exception.InvalidDateException;
import ru.practicum.stats.server.mapper.EndpointHitMapper;
import ru.practicum.stats.server.mapper.ViewStatsMapper;
import ru.practicum.stats.server.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<ViewStatsDto> getStats(String start,
                                             String end,
                                             List<String> uris,
                                             Boolean unique) {
        if (start == null || end == null) {
            throw new InvalidDateException("Даты указаны неверно");
        }

        LocalDateTime startDate = LocalDateTime.parse(decode(start), dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(decode(end), dateTimeFormatter);

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateException("Даты указаны неверно");
        }

        if (uris == null) {
            if (unique) {
                return statsRepository.findUniqueStats(startDate, endDate)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findAllStats(startDate, endDate)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            }
        } else {
            if (unique) {
                return statsRepository.findUniqueStatsWithUri(startDate, endDate, uris)
                        .stream()
                        .map(ViewStatsMapper::toViewStatsDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findAllStatsWithUri(startDate, endDate, uris)
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

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

}
