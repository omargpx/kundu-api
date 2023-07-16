package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDao extends JpaRepository<Member,Integer> {
}
