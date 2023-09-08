package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Book;
import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface ThemesContentService {
    void subscribe(int phase, Group group);
    List<Lesson> getLessonsUnavailable();
    List<Book> getBooksUnavailable();
    List<Lesson> getLessonAvailable();
    List<Book> getBooksAvailable();

    // others
    Lesson getLessonByCode(String code);
    Optional<Book> getBookById(int id);
}
