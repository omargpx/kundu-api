package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAX_SESSIONS")
public class Session implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session", nullable = false)
    private Integer id;
    @Column(name = "co_lesson", nullable = false)
    private String lessonCode;
    @Column(name = "fe_execution", nullable = false)
    private LocalDate execution;
    @Column(name = "es_session")
    private int status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_group_id", nullable = false)
    private Group groupSession;
}
