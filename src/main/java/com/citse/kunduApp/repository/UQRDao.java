package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.UserQuizResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UQRDao extends JpaRepository<UserQuizResult,Integer> {
}
