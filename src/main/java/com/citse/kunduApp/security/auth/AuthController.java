package com.citse.kunduApp.security.auth;

import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final KunduUtilitiesService kus;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/invitation")
    public ResponseEntity<?> verifyAccountInvitation(@RequestParam(name = "email")String email,
                                                     HttpServletRequest request){
        return ResponseEntity.ok(kus.getResponse(request, Services.AUTH_SERVICE.name(), service.verifyAccountInvitation(email),HttpStatus.OK));
    }

    @GetMapping("/exists")
    public ResponseEntity<?> verifyExistsUser(@RequestParam(name = "username",required = false)String username,
                                              @RequestParam(name = "phone",required = false)String phone,
                                              HttpServletRequest request){
        if(null!=username)
            return ResponseEntity.ok(kus.getResponse(request, Services.AUTH_SERVICE.name(), service.verifyExistsUsername(username),HttpStatus.OK));
        if(null!=phone)
            return ResponseEntity.ok(kus.getResponse(request, Services.AUTH_SERVICE.name(), service.verifyExistsPhoneNumber(phone),HttpStatus.OK));
        return ResponseEntity.ok(kus.getResponse(request, Services.AUTH_SERVICE.name(), "Choose an option",HttpStatus.PARTIAL_CONTENT));
    }
}
