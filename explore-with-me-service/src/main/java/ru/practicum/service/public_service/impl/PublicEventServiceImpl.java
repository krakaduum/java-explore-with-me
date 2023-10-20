package ru.practicum.service.public_service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.EventStatus;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.public_service.PublicEventService;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<EventShortDto> getEvents(String text,
                                               Collection<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Boolean onlyAvailable,
                                               String sortBy,
                                               Integer from,
                                               Integer size,
                                               HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new InvalidDateException("Даты указаны неверно");
            }
        }

        Collection<Event> events;
        if (sortBy != null) {
            if (sortBy.equals("EVENT_DATE")) {
                events = eventRepository
                        .findEventsSortedByDate(text, categories, paid, rangeStart, rangeEnd,
                                PageRequest.of(from, size))
                        .stream()
                        .collect(Collectors.toList());
            } else if (sortBy.equals("VIEWS")) {
                events = eventRepository
                        .findEventsSortedByViews(text, categories, paid, rangeStart, rangeEnd,
                                PageRequest.of(from, size))
                        .stream()
                        .collect(Collectors.toList());
            } else {
                throw new InvalidParamException("Неверный параметр сортировки");
            }
        } else {
            events = eventRepository
                    .findEvents(text, categories, paid, rangeStart, rangeEnd,
                            PageRequest.of(from, size))
                    .stream()
                    .collect(Collectors.toList());
        }

        if (onlyAvailable) {
            events = events
                    .stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        statsClient
                .create(new EndpointHitDto("ewm-main-service",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now().format(dateTimeFormatter)));

        return events
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(long id, HttpServletRequest request) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty() ||
                !event.get().getState().equals(EventStatus.PUBLISHED.toString())) {
            throw new NoSuchElementException("События с идентификатором " + id + " не существует");
        }
        statsClient
                .create(new EndpointHitDto("ewm-main-service",
                        request.getRequestURI(),
                        request.getRemoteAddr(),
                        LocalDateTime.now().format(dateTimeFormatter)));

        event.get().setViews(checkViews(request));

        return EventMapper.toEventFullDto(eventRepository.save(event.get()));

    }

    private long checkViews(HttpServletRequest request) {
        ResponseEntity<Object> stats = statsClient
                .readAll(
                        LocalDateTime.now().minusYears(100).format(dateTimeFormatter),
                        LocalDateTime.now().plusHours(1).format(dateTimeFormatter),
                        List.of(request.getRequestURI()),
                        true);

        ObjectMapper mapper = new ObjectMapper();
        List<ViewStatsDto> viewStatsDtos = mapper
                .convertValue(stats.getBody(), new TypeReference<>() {
                });

        return !viewStatsDtos.isEmpty() ? viewStatsDtos.get(0).getHits() : 0;
    }
}
