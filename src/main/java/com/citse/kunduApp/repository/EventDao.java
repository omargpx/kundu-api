package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Entities;
import com.citse.kunduApp.entity.Event;
import com.citse.kunduApp.utils.models.EventType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDao extends JpaRepository<Event,Integer> {
    List<Event> findAllByStatusIsTrueAndEntity(Entities entity);

    @Query("SELECT e FROM Event e WHERE e.status=true AND"+
    "(e.typeEvent = :typeEvent OR e.name LIKE %:name%) ORDER BY e.date DESC")
    List<Event> findAllEventsByFilters(@Param("typeEvent") EventType typeEvent,
                                       @Param("name") String name, Pageable pageable);
}
