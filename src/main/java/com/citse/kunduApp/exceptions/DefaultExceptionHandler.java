package com.citse.kunduApp.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(value = {ConfigDataResourceNotFoundException.class})
    public ResponseEntity<ApiError> handleException(ConfigDataResourceNotFoundException e,
                                                    HttpServletRequest request){
        ApiError apiError = new ApiError(request.getRequestURI(),
                e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InsufficientAuthenticationException.class})
    public ResponseEntity<ApiError> handleException(InsufficientAuthenticationException authException,
                                                    HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                authException.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ApiError> handleException(BadCredentialsException e,
                                                    HttpServletRequest request){
        ApiError apiError = new ApiError(request.getRequestURI(),
                e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {KunduException.class})
    public ResponseEntity<ApiError> handleException(KunduException ke,
                                                    HttpServletRequest request){
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                ke.getMessage(),
                ke.getStatus().value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError,ke.getStatus());
    }
}
