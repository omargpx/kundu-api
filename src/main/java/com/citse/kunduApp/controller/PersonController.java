package com.citse.kunduApp.controller;

import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                    HttpServletRequest request){
        if(null!=id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id), HttpStatus.OK));
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getAll(),HttpStatus.OK));
    }
}
