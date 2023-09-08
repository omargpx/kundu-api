package com.citse.kunduApp.exceptions;


public record AuthJWTResponse(String origin, String message, int status) {
}
