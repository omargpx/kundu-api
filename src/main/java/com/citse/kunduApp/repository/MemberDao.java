package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberDao extends JpaRepository<Member,Integer> {
    Optional<Member> findByPerson(Person person);
}
