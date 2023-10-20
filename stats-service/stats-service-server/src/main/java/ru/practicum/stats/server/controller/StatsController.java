package ru.practicum.stats.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.service.StatsService;

import java.util.Collection;
import java.util.List;

@RestController
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ViewStatsDto> getStats(@RequestParam(required = false) String start,
                                             @RequestParam(required = false) String end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.saveHit(endpointHitDto);
    }

}
