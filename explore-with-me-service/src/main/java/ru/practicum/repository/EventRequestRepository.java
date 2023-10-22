package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventRequest;

import java.util.Collection;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {
    Collection<EventRequest> findAllByRequesterIdAndEventId(Long userId, Long eventId);

    Collection<EventRequest> findAllByRequesterId(Long userId);

    Collection<EventRequest> findAllByEventId(Long eventId);
}
