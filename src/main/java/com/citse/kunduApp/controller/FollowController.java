package com.citse.kunduApp.controller;

import com.citse.kunduApp.utils.contracts.FollowService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private FollowService service;

    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.FOLLOW_SERVICE.name();

    @PostMapping("/set")
    public ResponseEntity<?> setFollow(@RequestParam("from")int from,
                                       @RequestParam("to")int to, HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.save(from,to), HttpStatus.OK));
    }

}
