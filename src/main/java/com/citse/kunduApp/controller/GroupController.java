package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.utils.contracts.GroupService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
                                    HttpServletRequest request){
        if(null!=code)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getByCode(code), HttpStatus.OK));
        if(null!= id)
            return ResponseEntity.ok(kus.getResponse(request,origin,service.getById(id), HttpStatus.OK));
        return ResponseEntity.ok(kus.getResponse(request,origin,service.getAll(), HttpStatus.OK));
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

}
