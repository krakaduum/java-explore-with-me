package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.stats.server.model.ViewStats(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM EndpointHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri IN ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY (hits.uri) DESC")
    Collection<ViewStats> findAllStatsWithUri(LocalDateTime start,
                                              LocalDateTime end,
                                              Collection<String> uris);

    @Query("SELECT new ru.practicum.stats.server.model.ViewStats(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM EndpointHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "AND hits.uri in ?3 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits.ip) DESC")
    Collection<ViewStats> findUniqueStatsWithUri(LocalDateTime start,
                                                 LocalDateTime end,
                                                 Collection<String> uris);

    @Query("SELECT new ru.practicum.stats.server.model.ViewStats(" +
            "hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM EndpointHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY (hits.uri) DESC")
    Collection<ViewStats> findAllStats(LocalDateTime start,
                                       LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.server.model.ViewStats(" +
            "hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM EndpointHit hits " +
            "WHERE hits.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hits.app, hits.uri " +
            "ORDER BY COUNT(DISTINCT hits.ip) DESC")
    Collection<ViewStats> findUniqueStats(LocalDateTime start,
                                          LocalDateTime end);
}
