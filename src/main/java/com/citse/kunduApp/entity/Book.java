package com.citse.kunduApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAX_BOOKS")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Integer id;
    @Column(name = "no_book")
    private String name;
    @Column
    private String description;
    @Column
    private int phase;
    @Column(nullable = false)
    private String code;
    @Column(name = "is_selected")
    private boolean selected;
    @Column
    private int pages;

    @OneToMany(mappedBy = "book")
    private List<Lesson> lessons;
}
