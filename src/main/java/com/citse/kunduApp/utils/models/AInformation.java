package com.citse.kunduApp.utils.models;

import java.time.LocalDateTime;

public record AInformation(String path, String message,Object detail ,int statusCode, LocalDateTime localDateTime) {
}
