package com.citse.kunduApp.utils.models;

import com.citse.kunduApp.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonIgnoreProperties({"secure","password","enabled",
            "credentialsNonExpired","accountNonExpired","authorities",
            "accountNonLocked","invitations","guests"})
    private User user;
}
