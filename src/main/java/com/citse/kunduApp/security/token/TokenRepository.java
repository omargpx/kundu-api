package com.citse.kunduApp.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    @Transactional
    void deleteTokensByUserId(Integer id);

    @Query("SELECT t FROM Token t JOIN t.user u WHERE u.username = :username AND (t.expired = false OR t.revoked = false)")
    Optional<Token> findTokenOnByUser(String username);
    Optional<Token> findByToken(String token);
}
