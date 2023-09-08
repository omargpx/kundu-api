package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    //region ATTRIBUTES
    @Autowired
    private PersonService service;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.PERSON_SERVICE.name();
    //endregion

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "id",required = false)Integer id,
                                    @RequestParam(name = "code",required = false)String code,
                                    @RequestParam(name = "page",required = false)Integer page,
                                    @RequestParam(name = "search",required = false)String query,
                                    HttpServletRequest request){
        if(null!=id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id),HttpStatus.OK));
        if(null!=code)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.findByKunduCode(code),HttpStatus.OK));
        if(null!=page){
            Pageable pageable = PageRequest.of(page,50);
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getPeoplePages(pageable),HttpStatus.OK));
        }
        if(null!=query)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.searchPerson(query),HttpStatus.OK));
        throw new KunduException(origin,"Kundu-api at your command",HttpStatus.PARTIAL_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<?> allPersonFilters(@RequestParam(name = "q",required = false)String query,
                                              @RequestParam(name = "p",required = false)String phone,
                                              HttpServletRequest request){
        if(null!=query)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.searchPerson(query),HttpStatus.OK));
        if(null!=phone)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.findByPhone(phone),HttpStatus.OK));
        throw new KunduException(origin,"Kundu-api at your command",HttpStatus.PARTIAL_CONTENT);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updatePerson(@PathVariable int id, @RequestBody Person person,
                                          HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.update(id,person),HttpStatus.OK));
    }
}
