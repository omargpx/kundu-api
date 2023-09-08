package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAX_LESSONS")
public class Lesson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lesson")
    private Integer id;
    @Column(name = "no_lesson")
    private String title;
    @Column
    private String verse;
    @Column(name = "desc_content")
    private String content;
    @Column
    private String reflection;
    @Column(nullable = false)
    private String code;
    @Column(name = "is_selected")
    private boolean selected;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
