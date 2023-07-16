package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Entities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityDao extends JpaRepository<Entities,Integer> {
}
