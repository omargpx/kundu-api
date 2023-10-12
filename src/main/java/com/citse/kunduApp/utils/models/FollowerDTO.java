package com.citse.kunduApp.utils.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FollowerDTO {
    private Integer id;
    private LocalDate date;
    private SimplePerson person;
}
