package com.citse.kunduApp.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class KunduException extends RuntimeException{
    private String path;
    private Object detail;
    private HttpStatus status;
    private LocalDateTime localDateTime;

    public KunduException(String path,String message, Object detail, HttpStatus status, LocalDateTime localDateTime) {
        super(message);
        this.path = path;
        this.detail = detail;
        this.status = status;
        this.localDateTime = localDateTime;
    }

}
