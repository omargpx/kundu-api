package com.citse.kunduApp.security.mock;

import com.citse.kunduApp.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TAX_INVITATIONS")
public class Invitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invitation")
    private Integer id;
    @Column(name = "reserve_email")
    private String email;
    @Column(name = "fe_invitation")
    private LocalDate date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User userReserve;
}
