package com.citse.kunduApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "tar_users_space")
public class Listener implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_space")
    private int id;
    @Column(name = "uuid_agora")
    private String uuid;

    @JsonIgnoreProperties({"invitations","guests","secure","password","enabled",
            "credentialsNonExpired","accountNonExpired","authorities","accountNonLocked"})
    @ManyToOne
    @JsonProperty("user")
    @JoinColumn(name = "user_id")
    private User userSpace;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;
}
