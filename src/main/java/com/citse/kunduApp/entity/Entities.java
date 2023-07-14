package com.citse.kunduApp.entity;

import com.citse.kunduApp.utils.models.TypeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TMA_ENTITIES")
public class Entities implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entity",nullable = false)
    private Integer id;
    @Column(name = "no_entity",nullable = false)
    private String name;
    @Column(name = "co_alias")
    private String alias;
    @Column(name = "url_image",nullable = false)
    private String logo;
    @Column(name = "fe_instance",nullable = false)
    private LocalDate creation;

    @Enumerated(EnumType.STRING)
    private TypeEntity typeEntity;

    @ManyToOne
    @JoinColumn(name = "fk_father_id",referencedColumnName = "id_entity")
    private Entities father;

    @JsonIgnoreProperties({"entity"})
    @OneToMany(mappedBy = "entity")
    private List<Event> events;

    @JsonIgnore
    @OneToMany(mappedBy = "gEntity")
    private List<Group> groups;
}
