package com.citse.kunduApp.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TAX_SPACES")
public class Space implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_space")
    private Integer id;
    @Column(name = "no_space")
    private String name;
    @Column(name = "fe_creation")
    private LocalDate creation; //TODO:change to DATETIME and after 5 hours from creation delete space
    @Column(name = "co_space")
    private String code;
    @Column(name = "token")
    private String token;

    @JsonIgnoreProperties({"invitations","guests","secure","password","enabled",
            "credentialsNonExpired","accountNonExpired","authorities","accountNonLocked"})
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @JsonIgnore
    @OneToMany(mappedBy = "space")
    private List<Listener> audience;
}
