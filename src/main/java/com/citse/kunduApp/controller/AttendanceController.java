package com.citse.kunduApp.controller;

import com.citse.kunduApp.entity.UserQuizResult;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.UQRService;
import com.citse.kunduApp.utils.models.AttendanceType;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    //region ATTRIBUTES
    @Autowired
    private UQRService uqrService;
    @Autowired
    private KunduUtilitiesService kus;
    private static final String origin = Services.USER_QUIZ_RESULTS.name();
    //endregion

    @GetMapping("/filter")
    public ResponseEntity<?> getAllByGroup(@RequestParam(name = "group",required = false)Integer idGroup,
                                           @RequestParam(name = "id",required = false)Integer id,
                                           HttpServletRequest request){
        if(null!=idGroup)
            return ResponseEntity.ok(kus.getResponse(request,origin,uqrService.getAllByGroup(idGroup), HttpStatus.OK));
        if(null!=id)
            return ResponseEntity.ok(kus.getResponse(request,origin,uqrService.getById(id),HttpStatus.OK));
        return ResponseEntity.ok(kus.getResponse(request,origin,"choose one",HttpStatus.OK));
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestParam(name = "sessionId")Integer sessionId,
                                  @RequestParam(name = "memberId")Integer memberId,
                                  @RequestParam(name = "ty") String type,
                                  @RequestBody(required = false) UserQuizResult uqr, HttpServletRequest request){
        var tyUp = type.toUpperCase();
        if(tyUp.equals(AttendanceType.QR.name()))
            return ResponseEntity.ok(kus.getResponse(request,origin,uqrService.saveQRAssist(sessionId,memberId),HttpStatus.OK));
        if(tyUp.equals(AttendanceType.QUIZ.name()) && null!=uqr)
            return ResponseEntity.ok(kus.getResponse(request,origin,uqrService.save(uqr,memberId,sessionId),HttpStatus.OK));
        if(!Objects.equals(tyUp, AttendanceType.QUIZ.name()) && uqr==null)
            throw new KunduException(origin,"¡this type requires body! - ",HttpStatus.BAD_REQUEST);
        throw new KunduException(origin,"¡Invalid attendance type! - ",HttpStatus.BAD_REQUEST);
    }

}
