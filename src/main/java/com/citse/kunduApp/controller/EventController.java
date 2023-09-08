package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Event;
import com.citse.kunduApp.utils.contracts.EventService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService service;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.EVENT_SERVICE.name();

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "id",required = false)Integer id,
                                    HttpServletRequest request){
        if(null!=id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id), HttpStatus.OK));
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getAll(), HttpStatus.OK));
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> save(@RequestBody Event event,HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.save(event), HttpStatus.OK));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable int id ,@RequestBody Event event,
                                    HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.update(id,event), HttpStatus.OK));
    }
}
