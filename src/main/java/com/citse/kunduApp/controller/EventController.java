package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Event;
import com.citse.kunduApp.utils.contracts.EventService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.EventType;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/** Event class controller
 * */
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService service;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.EVENT_SERVICE.name();

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "id", required = false)Integer id,
                                    @RequestParam(name = "code", required = false)String codeGroup,
                                    @RequestParam(name = "pg", required = false)Integer pg,
                                    @RequestParam(name = "entity", required = false)Integer entity,
                                    HttpServletRequest request){
        if(null!=id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id), HttpStatus.OK));
        if(null!=codeGroup)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getEventsByGroup(codeGroup), HttpStatus.OK));
        Pageable page = pg != null ? PageRequest.of(pg, 100) : PageRequest.of(0, 100);
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getAll(page), HttpStatus.OK));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPublic(@RequestParam(name = "typeEvent", required = false)EventType typeEvent,
                                          @RequestParam(name = "name", required = false)String name,
                                          @RequestParam(name = "entity", required = false)Integer entity,
                                          @RequestParam(name = "limit", required = false)Integer limit,
                                          HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getByAllFilters(typeEvent,name, Objects.requireNonNullElse(limit,10)), HttpStatus.OK));
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> save(@RequestBody Event event,HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.save(event), HttpStatus.OK));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Event event,
                                    HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, origin, service.update(id, event), HttpStatus.OK));
    }
}
