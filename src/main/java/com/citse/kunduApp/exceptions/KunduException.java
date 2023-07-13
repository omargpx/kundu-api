package com.citse.kunduApp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class KunduException extends RuntimeException{
    private String origin;
    private HttpStatus status;

    public KunduException(String origin,String message, HttpStatus status) {
        super(message);
        this.origin = origin;
        this.status = status;
    }

}
