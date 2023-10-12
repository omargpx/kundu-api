package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Listener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListenerDao extends JpaRepository<Listener,Integer> {
}
