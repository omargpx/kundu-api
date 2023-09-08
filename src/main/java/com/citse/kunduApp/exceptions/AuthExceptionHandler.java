package com.citse.kunduApp.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@RestControllerAdvice
public class AuthExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredTokenAuth(ExpiredJwtException ex,
                                                            HttpServletRequest request){
        ApiError apiError = new ApiError(request.getRequestURI(),
                ex.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
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
