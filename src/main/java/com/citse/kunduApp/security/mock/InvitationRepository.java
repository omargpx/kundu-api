package com.citse.kunduApp.security.mock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation,Integer> {
    Optional<Invitation> findByEmail(String email);
}
