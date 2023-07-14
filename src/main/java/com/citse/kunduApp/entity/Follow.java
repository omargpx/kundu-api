package com.citse.kunduApp.entity;

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
@Table(name = "TAX_FOLLOWS")
public class Follow implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_follow")
    private Integer id;
    @Column(name = "fe_follow", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "fk_follower_id")
    private Person follower;

    @ManyToOne
    @JoinColumn(name = "fk_followed_id")
    private Person followed;
}
