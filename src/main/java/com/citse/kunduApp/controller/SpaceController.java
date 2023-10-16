package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Space;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.SpaceService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public ResponseEntity<?> getSpaces(@RequestParam(name = "userId",required = false)Integer userId,
                                       @RequestParam(name = "pg",required = false)Integer pg,
                                       HttpServletRequest request){
        if(userId!=null)
            return ResponseEntity.ok(kus.getResponse(request,Services.SPACE_SERVICE.name(), service.getSpacesAround(userId),HttpStatus.OK));
        Pageable page = pg != null ? PageRequest.of(pg, 100) : PageRequest.of(0, 100);
        return ResponseEntity.ok(kus.getResponse(request, Services.SPACE_SERVICE.name(), service.getListSpaces(page.getPageNumber(), page.getPageSize()), HttpStatus.OK));
    }

    @PutMapping("/{id}/closed")
    public ResponseEntity<?> closeSpace(@PathVariable Integer id, HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, Services.SPACE_SERVICE.name(), service.closeSpace(id), HttpStatus.OK));
    }

    @GetMapping("/history")
    public ResponseEntity<?> closedSpaces(@RequestParam(name = "moderator")Integer id,HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, Services.SPACE_SERVICE.name(), service.historySpacesFromUser(id), HttpStatus.OK));
    }
}
