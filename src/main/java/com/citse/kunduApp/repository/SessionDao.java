package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionDao extends JpaRepository<Session,Integer> {
    Session findByLessonCode(String code);
}
