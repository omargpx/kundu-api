package com.citse.kunduApp.entity;

import com.citse.kunduApp.security.mock.Invitation;
import com.citse.kunduApp.security.token.Token;
import com.citse.kunduApp.utils.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TSG_USERS")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;
    @Column(name = "no_username", nullable = false)
    private String username;
    @Column(name = "kt_password",nullable = false)
    private String password;
    @Column(name = "di_email",nullable = false)
    private String email;
    @Column(name = "co_secure_pass")
    private Integer secure;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "fe_last_connect")
    private LocalDateTime lastConnect;

    @JsonIgnore
    @OneToOne(mappedBy = "userDetail", cascade = CascadeType.MERGE)
    private Person person;

    @JsonIgnoreProperties({"userReserve"})
    @OneToMany(mappedBy = "userReserve")
    private List<Invitation> invitations;

    @Transient
    @Builder.Default
    @JsonIgnoreProperties({"member"})
    private List<Object> guests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @JsonIgnore
    @OneToMany(mappedBy = "moderator")
    private List<Space> spaces;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id.equals(user.id);
    }
}
