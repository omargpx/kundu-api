package com.citse.kunduApp.entity;

import com.citse.kunduApp.utils.models.EventType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TMA_EVENTS")
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event")
    private Integer id;
    @Column(name = "no_event")
    private String name;
    @Column(name = "dc_event")
    private String description;
    @Column(name = "no_place")
    private String place;
    @Enumerated(EnumType.STRING)
    private EventType typeEvent;
    @Column(name = "fe_execution")
    private LocalDateTime date;

    @JsonIgnoreProperties({"events","groups","father"})
    @ManyToOne
    @JoinColumn(name = "fk_entity_id")
    private Entities entity;
}
