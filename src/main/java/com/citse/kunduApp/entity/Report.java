package com.citse.kunduApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TAX_REPORTS")
public class Report implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_report", nullable = false)
    private int id;
    @Column(name = "no_report", nullable = false)
    private String name;
    @Column(name = "desc_report")
    private String description;
    private String type;//TODO: enum type
    private User userReport;
}
