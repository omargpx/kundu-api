package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonDao extends JpaRepository<Person,Integer> {
    Person findByKunduCode(String kunduCode);
    @Query("SELECT p FROM Person p JOIN p.userDetail u WHERE u.username = :username")
    Person findPersonByUsername(@Param("username") String username);

    Optional<Person> findByPhone(String phone);

}
