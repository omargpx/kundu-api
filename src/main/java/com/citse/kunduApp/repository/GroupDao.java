package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao extends JpaRepository<Group,Integer> {
    Group findByCode(String code);
}
