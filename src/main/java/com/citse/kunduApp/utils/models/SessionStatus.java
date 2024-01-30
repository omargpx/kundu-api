package com.citse.kunduApp.utils.models;

import lombok.Getter;

@Getter
public enum SessionStatus {
    CLOSED(0, "Session is closed"),
    ON(1, "Session is on"),
    OFF(2, "Session is off");


    private final int value;
    private final String description;

    SessionStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
