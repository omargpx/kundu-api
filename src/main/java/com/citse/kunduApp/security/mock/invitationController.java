package com.citse.kunduApp.security.mock;

import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitation")
@RequiredArgsConstructor
public class invitationController {

    private final InvitationService service;
    private final KunduUtilitiesService kus;

    @PostMapping("/new")
    public ResponseEntity<?> reserve(@RequestParam(name = "email")String email,
                                     @RequestParam(name = "userId")Integer userId,
                                     HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, Services.AUTH_SERVICE.name(), service.save(email,userId), HttpStatus.OK));
    }
}
