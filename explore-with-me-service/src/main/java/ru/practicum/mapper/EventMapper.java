package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.model.Event;
import ru.practicum.model.enums.EventLifecycle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                Enum.valueOf(EventLifecycle.class, event.getState()),
                event.getTitle(),
                event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public static Event toEvent(EventShortDto eventShortDto) {
        return new Event(
                eventShortDto.getId(),
                eventShortDto.getAnnotation(),
                CategoryMapper.toCategory(eventShortDto.getCategory()),
                eventShortDto.getConfirmedRequests(),
                eventShortDto.getEventDate(),
                UserMapper.toUser(eventShortDto.getInitiator()),
                eventShortDto.getTitle(),
                eventShortDto.getViews());
    }
}
