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
@Table(name = "TMV_MEMBERS")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_member")
    private Integer id;
    @Column(name = "fe_instance")
    private LocalDate dateJoin;

    @JsonIgnoreProperties({"userDetail","member","following","followers","biography"})
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_person_id")
    private Person person;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_group_id")
    private Group group;
}
