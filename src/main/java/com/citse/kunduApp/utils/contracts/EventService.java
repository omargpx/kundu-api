package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Event;
import com.citse.kunduApp.utils.models.EventType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll(Pageable pageable);
    Event save(Event event);
    Event getById(int id);
    void delete(int id);
    Event update(int id, Event e);
    List<Event> getEventsByGroup(String codeGroup);
    List<Event> getByAllFilters(EventType eventType, String name, int limit);
}
