package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Group,Integer> {
    Group findByCode(String code);

    @Query("SELECT s FROM Session s WHERE s.groupSession = :group")
    List<Session> getSessionsByGroupCode(@Param("group")Group group);

    @Query(value = "SELECT m FROM Member m WHERE m.group = :group")
    List<Member> getMembersByGroup(@Param("group")Group group);

    @Query("DELETE FROM Session s WHERE s.groupSession = :group")
    void cleanSessions(@Param("group")Group group);

    List<Group> findAllGroupsByOrderByPointsDesc(Pageable pageable);

    @Query("SELECT g FROM Group g JOIN g.gEntity e WHERE e.id = :entityId AND g.phase = :phase ORDER BY g.points DESC")
    List<Group> findTop10ByEntityIdAndPhase(@Param("entityId") Integer entityId, @Param("phase") Integer phase);
}
