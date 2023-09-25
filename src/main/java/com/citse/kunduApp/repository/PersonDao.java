package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.utils.models.SimplePerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonDao extends JpaRepository<Person,Integer> {
    Person findByKunduCode(String kunduCode);
    @Query("SELECT p FROM Person p JOIN p.userDetail u WHERE u.username = :username")
    Person findPersonByUsername(@Param("username") String username);

    @Query("SELECT p FROM Person p LEFT JOIN p.userDetail u WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:query,'%'))" +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%',:query,'%'))")
    List<Person> searchByFullNameOrNickname(@Param("query")String query);

    Optional<Person> findByPhone(String phone);
    Page<SimplePerson> findAllBy(Pageable pageable);//TODO: FIX IT

}
