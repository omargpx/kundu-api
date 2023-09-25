package com.citse.kunduApp.utils.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SimplePerson {
    private Integer id;
    private String name;
    private String phone;
    private String avatar;
    private String kunduCode;
    private String biography;
    private int experience;
    private LocalDate birth;
    private LocalDate joinDate;
}
