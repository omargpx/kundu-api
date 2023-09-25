package com.citse.kunduApp.utils.models;

import com.citse.kunduApp.entity.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class SimpleMember implements Serializable {
    private Integer id;
    private LocalDate dateJoin;

    @JsonIgnoreProperties({"user","member","biography"})
    private Person person;
}
