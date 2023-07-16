package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowDao extends JpaRepository<Follow,Integer> {
}
