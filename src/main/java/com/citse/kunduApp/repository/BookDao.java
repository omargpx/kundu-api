package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book,Integer> {

    @Query("SELECT  b FROM Book b WHERE b.selected = :status")
    List<Book> findBooksByStatus(@Param("status")boolean status);

    @Query(value = """
            UPDATE tax_books
            SET is_selected = CASE
                WHEN (
                    SELECT COUNT(id_lesson)
                    FROM tax_lessons
                    WHERE book_id = 1
                    AND is_selected = false
                ) < 13 THEN true
                ELSE false
            END
            WHERE id_book = :bookId;""",nativeQuery = true)
    void detectAvailableLessonsIntoBook(@Param("bookId")Integer bookId);

    @Query(value = "SELECT * FROM tax_books b WHERE b.phase = :phase ORDER BY RAND() LIMIT 1;",nativeQuery = true)
    Optional<Book> getRandomBookByPhase(@Param("phase")Integer phase);
}
