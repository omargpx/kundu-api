package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Person;
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
    @Query("SELECT p FROM Person p JOIN p.userDetail u WHERE u.email = :email")
    Person findPersonByEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p LEFT JOIN p.userDetail u WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:query,'%'))" +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%',:query,'%'))")
    List<Person> searchByFullNameOrNickname(@Param("query")String query,Pageable pageable);
    @Query("SELECT p FROM Person p LEFT JOIN FETCH p.following WHERE p.id = :userId")
    List<Person> findPersonWithFollowingById(@Param("userId") Integer userId);
    Page<Person> findAllBy(Pageable pageable);
    @Query("SELECT DISTINCT p FROM Person p WHERE p.id NOT IN " +
            "(SELECT f.followed.id FROM Follow f WHERE f.follower.id = :id)")
    List<Person> getSuggestFriends(@Param("id") Integer id, Pageable pageable);

    Optional<Person> findByPhone(String phone);
    Optional<Person> findByMember(Member member);

}
