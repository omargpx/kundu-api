package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TMA_PEOPLE")
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person", nullable = false)
    private Integer id;
    @Column(name = "no_person", nullable = false)
    private String name;
    @Column(name = "nu_phone")
    private String phone;
    @Column(name = "url_avatar", nullable = false)
    private String avatar;
    @Column(name = "co_kundu", nullable = false, unique = true)
    private String kunduCode;
    @Column(name = "dc_biography")
    private String biography;
    @Column(name = "nu_experience")
    private int experience;
    @Column(name = "fe_birthday")
    private LocalDate birth;
    @Column(name = "fe_join_to_kundu")
    private LocalDate joinDate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "fk_user_id")
    private User user;
}
