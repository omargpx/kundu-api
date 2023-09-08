package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.ThemesContentService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/library")
public class BookController {

    @Autowired
    private ThemesContentService service;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.LIBRARY_SERVICE.name();
    private final ResourceLoader resourceLoader;

    public BookController(ResourceLoader resource) {
        this.resourceLoader = resource;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> SubscribeLessons(@RequestParam(name = "phase")Integer phase,
                                              @RequestParam(name = "groupId")Integer groupId,
                                              HttpServletRequest request){
        var group = Group.builder().id(groupId).build();
        service.subscribe(phase,group);
        return ResponseEntity.ok(kus.getResponse(request, origin,"Successful subscription", HttpStatus.OK));
    }

    @GetMapping("/lesson")
    public ResponseEntity<?> getLesson(@RequestParam(name = "code") String code,
                                       HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getLessonByCode(code),HttpStatus.OK));
    }

    @GetMapping("/book")
    public ResponseEntity<?> getBook(@RequestParam(name = "id",required = false)Integer id,
                                     HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getBookById(id),HttpStatus.OK));
    }

    @GetMapping(value = "/forms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleInfo(@RequestParam(name = "id") String code) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/shared/constants/"+code+".json");
        String jsonContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new ResponseEntity<>(jsonContent, HttpStatus.PARTIAL_CONTENT);
    }

}
