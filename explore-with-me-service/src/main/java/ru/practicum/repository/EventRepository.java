package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Set<Event> findAllByIdIn(Collection<Long> events);

    Collection<Event> findAllByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.eventDate")
    Page<Event> findEventsSortedByDate(String text, Collection<Long> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) " +
            "AND e.state = 'PUBLISHED' " +
            "ORDER BY e.views")
    Page<Event> findEventsSortedByViews(String text, Collection<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:text IS NULL) OR (upper(e.annotation) LIKE upper(concat('%', :text, '%')) " +
            "OR upper(e.description) LIKE upper(concat('%', :text, '%')))) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((:paid IS NULL) OR (e.paid = :paid)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) " +
            "AND e.state = 'PUBLISHED'")
    Page<Event> findEvents(String text, Collection<Long> categories, Boolean paid,
                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:users IS NULL) OR (e.initiator.id IN :users)) " +
            "AND ((:states IS NULL) OR (e.state IN :states)) " +
            "AND ((:categories IS NULL) OR (e.category.id IN :categories)) " +
            "AND ((CAST(:rangeStart AS date) IS NULL) OR (e.eventDate >= :rangeStart)) " +
            "AND ((CAST(:rangeEnd AS date) IS NULL) OR (e.eventDate <= :rangeEnd)) ")
    Page<Event> findEventsForAdmin(Collection<Long> users, Collection<String> states, Collection<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<Event> findEventsByInitiatorId(long userId, Pageable pageable);

    Collection<Event> findEventsByInitiatorId(long userId);

    Event findByInitiatorIdAndId(long userId, long eventId);
}
