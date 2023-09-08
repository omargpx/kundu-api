package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll();
    Event save(Event event);
    Event getById(int id);
    void delete(int id);

    Event update(int id, Event e);
}
