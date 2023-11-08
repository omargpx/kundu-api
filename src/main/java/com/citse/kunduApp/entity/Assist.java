package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "TAR_ASSISTS")
public class Assist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assist")
    private Integer id;
    @Column(name = "fe_register")
    private LocalDate date;
    @Column(name = "quiz_realized")
    private boolean quiz;
    @Column(name = "attendance")
    private boolean attendance;
    @JsonIgnoreProperties({"group","assists"})
    @ManyToOne
    @JoinColumn(name = "fk_member_id")
    private Member member;
    @JsonIgnoreProperties({"groupSession","status"})
    @ManyToOne
    @JoinColumn(name = "fk_session_id")
    private Session session;
}
