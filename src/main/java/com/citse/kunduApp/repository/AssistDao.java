package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Assist;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistDao extends JpaRepository<Assist,Integer> {
    Assist findByMemberAndSession(Member member, Session session);
}
