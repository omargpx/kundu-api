package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/* User Quiz Results Temporary for next quiz
* TAT -> Temporary Auxiliary Table
* */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TAT_VAL_QUIZ")
public class UserQuizResult implements Serializable {
    @Id
    @Column(name = "id_val_quiz")
    private int id;
    @Column(name = "title_lesson")
    private String title;
    @Column(name = "code_session")
    private String code;
    @Column
    private int questions;
    @Column
    private int answers;
    @Column(name = "xp_gained")
    private int xp;
    @Column(name = "points_group")
    private int points;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "fk_member_id")
    private Member memberResult;
}
