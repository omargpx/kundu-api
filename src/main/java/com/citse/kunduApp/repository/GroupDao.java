package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Session;
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

    @Query("DELETE FROM Session s WHERE s.groupSession = :group")
    void cleanSessions(@Param("group")Group group);
}
