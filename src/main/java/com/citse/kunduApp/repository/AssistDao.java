package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Assist;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Session;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistDao extends JpaRepository<Assist,Integer> {
    Assist findByMemberAndSession(Member member, Session session);

    @Query("SELECT a FROM Assist a WHERE a.member.id = :memberId AND a.session.id = :sessionId")
    Assist verifyAssist(@Param("memberId") int memberId, @Param("sessionId") int sessionId);
}
