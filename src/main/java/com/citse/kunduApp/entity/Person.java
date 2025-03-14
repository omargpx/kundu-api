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
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TMA_PEOPLE")
public class Person implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_person", nullable = false)
    private Integer id;
    @Column(name = "no_person", nullable = false)
    private String name;
    @Column(name = "nu_phone",nullable = false, unique = true)
    private String phone;
    @Column(name = "url_avatar", nullable = false)
    private String avatar;
    @Column(name = "co_kundu", nullable = false, unique = true)
    private String kunduCode;
    @Column(name = "dc_biography")
    private String biography;
    @Column(name = "nu_experience")
    private int experience;
    @Column(name = "fe_birthday")
    private LocalDate birth;
    @Column(name = "fe_join_to_kundu")
    private LocalDate joinDate;

    @JsonIgnoreProperties({"secure","password","enabled",
            "credentialsNonExpired","accountNonExpired","authorities","accountNonLocked"})
    @OneToOne
    @JsonProperty("user")
    @JoinColumn(name = "fk_user_id")
    private User userDetail;

    @JsonIgnore
    @OneToMany(mappedBy = "follower")
    private List<Follow> following;

    @JsonIgnore
    @OneToMany(mappedBy = "followed")
    private List<Follow> followers;

    @JsonIgnoreProperties({"person"})
    @OneToOne(mappedBy = "person", cascade = CascadeType.MERGE)
    private Member member;

    @Transient
    @JsonProperty("followers")
    private int numFollowers;
    @Transient
    @JsonProperty("following")
    private int numFollowing;

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id.equals(person.id);
    }
}
