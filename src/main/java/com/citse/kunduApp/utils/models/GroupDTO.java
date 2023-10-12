package com.citse.kunduApp.utils.models;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GroupDTO {
    private Integer id;
    private String name;
    private String code;
    private String lema;
    private String verse;
    private String song;
    private String tag;
    private String image;
    private Integer phase;
    private int level;
    private int points;
    private LocalDate creation;
    private Object entity;
    private List<SimpleMember> members;
}
