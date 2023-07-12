package com.citse.kunduApp.controller;

import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.AInformation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@RequestMapping
public class DefaultController {

    @Autowired
    private KunduUtilitiesService kus;
    private final ResourceLoader resourceLoader;

    public DefaultController(ResourceLoader resource) {
        this.resourceLoader = resource;
    }

    @RequestMapping
    public ResponseEntity<?> handleInit(HttpServletRequest request){
        AInformation information = new AInformation(
                request.getRequestURI(),
                "Welcome to Kundu Api!",
                kus.BrowserSpecifyHeaders(request),
                HttpStatus.OK.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(information,HttpStatus.OK);
    }

    @RequestMapping(value = "/info",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleInfo(HttpServletRequest request) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/shared/info.json");
        String jsonContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new ResponseEntity<>(jsonContent, HttpStatus.PARTIAL_CONTENT);
    }

    @RequestMapping(value = "/avatars",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleShowAvatars(HttpServletRequest request) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/shared/avatars.json");
        String jsonContent = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        return new ResponseEntity<>(jsonContent, HttpStatus.OK);
    }
}
