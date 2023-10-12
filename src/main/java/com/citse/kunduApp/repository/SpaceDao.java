package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpaceDao extends JpaRepository<Space,Integer> {
    Optional<Space> findByCode(String code);
}
