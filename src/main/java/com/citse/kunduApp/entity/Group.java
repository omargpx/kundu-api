package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TMA_GROUPS")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group",nullable = false)
    private Integer id;
    @Column(name = "no_group",nullable = false)
    private String name;
    @Column(name = "co_group",nullable = false, unique = true)
    private String code;
    @Column(name = "dc_lema",nullable = false)
    private String lema;
    @Column(name = "dc_verse",nullable = false)
    private String verse;
    @Column(name = "url_song",nullable = false)
    private String song;
    @Column(name = "no_tag")
    private String tag;
    @Column(name = "url_image",nullable = false)
    private String image;
    @Column(name = "nu_phase")
    private Integer phase;
    @Column(name = "nu_tier")
    private int level;
    @Column(name = "nu_points")
    private int points;
    @Column(name = "fe_instance", nullable = false)
    private LocalDate creation;

    @JsonIgnoreProperties({"groups","events","father"})
    @JsonProperty("entity")
    @ManyToOne
    @JoinColumn(name = "fk_entity_id")
    private Entities gEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "group",cascade = CascadeType.PERSIST)
    private List<Member> members;

    @JsonIgnore
    @OneToMany(mappedBy = "groupSession")
    private List<Session> sessions;
}
