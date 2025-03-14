package com.citse.kunduApp.security.auth;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.security.config.JwtService;
import com.citse.kunduApp.security.mock.InvitationRepository;
import com.citse.kunduApp.security.mock.VerifyResponse;
import com.citse.kunduApp.security.token.Token;
import com.citse.kunduApp.security.token.TokenRepository;
import com.citse.kunduApp.security.token.TokenType;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Role;
import com.citse.kunduApp.utils.models.Services;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDao userRepo;
    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final InvitationRepository invitationRepository;
    @Autowired
    private KunduUtilitiesService kus;
    @Autowired
    private PersonDao personRepo;


    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        verifyExistsUser(request.getEmail(), request.getPhone(),request.getUsername());// revoke if user already exists with these parameters
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole()!=null? request.getRole() : Role.USER)
                .lastConnect(LocalDateTime.now())
                .secure(request.getSecure())
                .isConnect(false)
                .build();
        var savedUser = userRepo.save(user);
        var person = Person.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .avatar(request.getAvatar())
                .kunduCode(kus.KunduCode("KSC"))
                .joinDate(LocalDate.now())
                .userDetail(savedUser)
                .build();
        personRepo.save(person);
        Map<String,Object> additionalInfo = new HashMap<>();
        additionalInfo.put("KunduCode",person.getKunduCode());
        var jwtToken = jwtService.generateToken(additionalInfo,user);
        saveUserToken(savedUser,jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            var jwtToken = jwtService.generateToken(getAdditionalInfoToken(request.getUsername()),user);
            tokenRepo.deleteTokensByUserId(user.getId());
            saveUserToken(user,jwtToken);
            return AuthenticationResponse.builder().accessToken(jwtToken).build();
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Bad credentials", e);
        }
    }
    private Map<String,Object> getAdditionalInfoToken(String username){
        Map<String,Object> additionalInfo = new HashMap<>();
        var person = personRepo.findPersonByUsername(username);
        additionalInfo.put("KunduCode",person.getKunduCode());
        return additionalInfo;
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(token);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userRepo.findByUsername(username)
                    .orElseThrow(()-> new KunduException(Services.AUTH_SERVICE.name(), "User by username not found",HttpStatus.NOT_FOUND));
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateRefreshToken(user);
                tokenRepo.deleteTokensByUserId(user.getId());
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public VerifyResponse verifyAccountInvitation(String email){
        var user = userRepo.findByEmail(email);
        if(user.isPresent())
            return VerifyResponse.builder().message("Already exists as user").isSuccess(false).build();
        var invitation_reserved = invitationRepository.findByEmail(email);
        if(invitation_reserved.isPresent())
            return VerifyResponse.builder().message("Go ahead").isSuccess(true).build();
        return VerifyResponse.builder().message("Unreserved").isSuccess(false).build();
    }

    public VerifyResponse verifyExistsUsername(String username) {
        var user = userRepo.findByUsername(username);
        if(user.isPresent())
            return VerifyResponse.builder().message("Username already exists as user").isSuccess(false).build();
        return VerifyResponse.builder().message("Go ahead").isSuccess(true).build();
    }

    private void verifyExistsUser(String email, String phone,String username){
        var verifyUserEmail = userRepo.findByEmail(email);
        var verifyPhoneNumber = personRepo.findByPhone(phone);
        var verifyUsername = userRepo.findByUsername(username);
        if(verifyUserEmail.isPresent() || verifyPhoneNumber.isPresent() || verifyUsername.isPresent())
            throw new KunduException(Services.AUTH_SERVICE.name(),"User already exists", HttpStatus.BAD_REQUEST);
    }

    public VerifyResponse verifyExistsPhoneNumber(String phone) {
        var verified = personRepo.findByPhone(phone);
        if (verified.isPresent())
            return VerifyResponse.builder().message("Phone number already in use").isSuccess(false).build();
        return VerifyResponse.builder().message("Go ahead").isSuccess(true).build();
    }
}
