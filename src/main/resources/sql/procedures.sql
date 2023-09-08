
-- PROCEDURE TO SUBSCRIBE 13 LESSONS
DELIMITER $$

CREATE PROCEDURE selectAvailableLessons(IN bookId INT)
BEGIN
    DECLARE lessIds VARCHAR(255);

    SELECT GROUP_CONCAT(id_lesson) INTO lessIds FROM tax_lessons WHERE book_id = bookId AND is_selected = false LIMIT 13;
    SELECT l.code
    FROM tax_lessons l
    JOIN
    (
        SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(lessIds, ',', numbers.n), ',', -1) AS lessonId
        FROM
        (
            SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL
            SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13
        ) numbers
        WHERE numbers.n <= 13
    ) tempTable ON tempTable.lessonId = l.id_lesson;

    UPDATE tax_lessons SET is_selected = true WHERE book_id = bookId AND id_lesson IN
    (
        SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(lessIds, ',', numbers.n), ',', -1) AS lessonId
        FROM
        (
            SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL
            SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12 UNION ALL SELECT 13
        ) numbers
        WHERE numbers.n <= 13
    );
END $$

DELIMITER ;