package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonDao extends JpaRepository<Lesson,Integer> {

    Optional<Lesson> findByCode(String code);

    @Query("SELECT l FROM Lesson l WHERE l.selected = :status")
    List<Lesson> findLessonsByStatus(@Param("status")boolean status);

    @Query(value = "CALL selectAvailableLessons(:bookId);",nativeQuery = true)
    List<String> subscribeCodeLessons(@Param("bookId")Integer bookId);
}
