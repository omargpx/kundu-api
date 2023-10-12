package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Space;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.SpaceService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/agora")
@RequiredArgsConstructor
public class SpaceController {
    private final KunduUtilitiesService kus;
    private final SpaceService service;

    @PostMapping("/create-space")
    public ResponseEntity<?> createSpace(@RequestBody Space space, HttpServletRequest request,
                                           @RequestParam(name = "userId")Integer userId){
        return ResponseEntity.ok(kus.getResponse(request, Services.SPACE_SERVICE.name(),service.create(space,userId), HttpStatus.OK));
    }

    @GetMapping("/spaces")
    public ResponseEntity<?> getSpaces(HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, Services.SPACE_SERVICE.name(), service.getSpaces(), HttpStatus.OK));
    }

}
