package com.citse.kunduApp.utils.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SimpleMember {
    private Integer id;
    private LocalDate dateJoin;
    private SimplePerson person;
}
