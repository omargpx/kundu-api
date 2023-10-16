package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Entities;
import com.citse.kunduApp.entity.Event;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.EntityDao;
import com.citse.kunduApp.repository.EventDao;
import com.citse.kunduApp.repository.GroupDao;
import com.citse.kunduApp.utils.contracts.EventService;
import com.citse.kunduApp.utils.models.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventImp implements EventService {

    @Autowired
    private EventDao repo;
    @Autowired
    private GroupDao groupRepo;
    @Autowired
    private EntityDao entityRepo;

    @Override
    public List<Event> getAll(Pageable pageable) {
        Page<Event> events = repo.findAll(pageable);
        if(!events.isEmpty())
            return events.getContent();
        throw new KunduException(Services.EVENT_SERVICE.name(),"Events are empty", HttpStatus.NO_CONTENT);
    }

    @Override
    public Event save(Event event) {
        return repo.save(event);
    }

    @Override
    public Event getById(int id) {
        Optional<Event> event = repo.findById(id);
        return event.orElseThrow(()-> new KunduException(Services.EVENT_SERVICE.name(), "Event not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public Event update(int id, Event e) {
        Event event = getById(id);
        event.setName(e.getName() != null? e.getName() : event.getName());
        event.setDate(e.getDate() != null? e.getDate() : event.getDate());
        event.setTypeEvent(e.getTypeEvent() != null? e.getTypeEvent() : event.getTypeEvent());
        event.setPlace(e.getPlace() != null? e.getPlace() : event.getPlace());
        event.setEntity(e.getEntity() != null? e.getEntity() : event.getEntity());
        event.setDescription(e.getDescription() != null? e.getDescription() : event.getDescription());
        return repo.save(event);
    }

    @Override
    public List<Event> getEventsByGroup(String codeGroup) {
        var group = groupRepo.findByCode(codeGroup);
        List<Event> allEvents = new ArrayList<>();
        if(null==group)
            throw new KunduException(Services.EVENT_SERVICE.name(), "Group not found",HttpStatus.NOT_FOUND);
        List<Entities> fatherEntities = getAllFathersEntities(group.getGEntity().getId());

        for (Entities entity : fatherEntities){
            List<Event> events = repo.findAllByStatusIsTrueAndEntity(entity);
            allEvents.addAll(events);
        }
        return allEvents;
    }

    /* Filter all entity fathers where [entityId]
    * */
    private List<Entities> getAllFathersEntities(int entityId){
        List<Entities> fathers = new ArrayList<>();
        Entities entity = entityRepo.findById(entityId)
                .orElseThrow(() -> new KunduException(Services.EVENT_SERVICE.name(),"Entity not found", HttpStatus.NOT_FOUND));
        while (entity != null) {
            fathers.add(entity);
            entity = entity.getFather();
        }
        return fathers;
    }
}
