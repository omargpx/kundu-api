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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    @Column(name = "desc_space")
    private String description;
    @Column(name = "fe_creation")
    private LocalDateTime creation; //TODO:change to DATETIME and after 5 hours from creation delete space
    @Column(name = "co_space")
    private String code;
    @Column(name = "token")
    private String token;
    @Column(name = "status")
    private boolean status;

    @JsonIgnoreProperties({"invitations","guests","secure","password","enabled",
            "credentialsNonExpired","accountNonExpired","authorities","accountNonLocked"})
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @JsonIgnore
    @OneToMany(mappedBy = "space")
    private List<Listener> audience;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return Objects.equals(id, space.id); // 'o' compare other unique fields if not the id
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
