package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.utils.contracts.GroupService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    //region ATTRIBUTES
    @Autowired
    private GroupService service;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.GROUP_SERVICE.name();
    //endregion

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "code",required = false)String code,
                                    @RequestParam(name = "id",required = false)Integer id,
                                    @RequestParam(name = "page",required = false)Integer page,
                                    HttpServletRequest request){
        if(null!=code)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getByCode(code), HttpStatus.OK));
        if(null!= id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id), HttpStatus.OK));
        if(null!=page){
            Pageable pageable = PageRequest.of(page,50);
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getGroupPages(pageable), HttpStatus.OK));
        }
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getGroupPages(PageRequest.of(0,50)), HttpStatus.OK));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<?> save(@RequestBody Group group,HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.save(group),HttpStatus.OK));
    }

    @PostMapping("/join")
    public ResponseEntity<?> applyJoinGroup(@RequestParam(name = "code")String code,
                                            @RequestParam(name = "kunduCode")String kunduCode,
                                            HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.applyJoinGroup(code,kunduCode),HttpStatus.OK));
    }

    @GetMapping("/{code}/lessons")
    public ResponseEntity<?> getSessionsByGroupCode(@PathVariable String code,
                                                   HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getSessionFromGroupByCode(code),HttpStatus.OK));
    }


    @GetMapping("/{code}/members")
    public ResponseEntity<?> getListMembers(@PathVariable String code, HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getMembersByGroupCode(code),HttpStatus.OK));
    }
}
