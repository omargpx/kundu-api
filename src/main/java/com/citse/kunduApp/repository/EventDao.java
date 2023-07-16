package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Event,Integer> {
}
