package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.*;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.*;
import com.citse.kunduApp.utils.contracts.ThemesContentService;
import com.citse.kunduApp.utils.models.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ThemesContentImp implements ThemesContentService {

    @Autowired
    private BookDao bookRepo;
    @Autowired
    private LessonDao lessonRepo;
    @Autowired
    private SessionDao sessionRepo;
    @Autowired
    private MemberDao memberRepo;
    @Autowired
    private AssistDao assistRepo;

    @Override
    public void subscribe(int phase, Group group) {
        if(phase < 1 || phase >5)
            throw new KunduException(Services.LIBRARY_SERVICE.name(), "Incorrect phase",HttpStatus.BAD_REQUEST);
        var randomBook = bookRepo.getRandomBookByPhase(phase);
        if(randomBook.isEmpty())
            throw new KunduException(Services.LIBRARY_SERVICE.name(), "No books for phase "+phase,HttpStatus.NOT_FOUND);
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            List<String> codeLessons = lessonRepo.subscribeCodeLessons(randomBook.get().getId());
            AtomicInteger count = new AtomicInteger(0);
            codeLessons.forEach( code -> {
                var session = Session.builder()
                        .execution(LocalDate.now().plusWeeks(count.getAndIncrement()))
                        .lessonCode(code)
                        .status(0)
                        .groupSession(group)
                        .build();
                sessionRepo.save(session);
            });
            future.complete(null);
        }).thenRunAsync(() -> {
            bookRepo.detectAvailableLessonsIntoBook(randomBook.get().getId());
        });
    }

    @Override
    public List<Lesson> getLessonsUnavailable() {
        List<Lesson> lessons = lessonRepo.findLessonsByStatus(false);
        if(!lessons.isEmpty())
            return lessons;
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"No unavailable lessons", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Book> getBooksUnavailable() {
        List<Book> books = bookRepo.findBooksByStatus(false);
        if(!books.isEmpty())
            return books;
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"No unavailable books", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Lesson> getLessonAvailable() {
        List<Lesson> lessons = lessonRepo.findLessonsByStatus(true);
        if(!lessons.isEmpty())
            return lessons;
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"No available lessons", HttpStatus.NO_CONTENT);
    }

    @Override
    public List<Book> getBooksAvailable() {
        List<Book> books = bookRepo.findBooksByStatus(true);
        if(!books.isEmpty())
            return books;
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"No available books", HttpStatus.NO_CONTENT);
    }

    @Override
    public Lesson getLessonByCode(String code) {
        Optional<Lesson> lesson = lessonRepo.findByCode(code);
        if(lesson.isPresent())
            return lesson.get();
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"Lesson with code: '"+code+"' not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public Optional<Book> getBookById(int id) {
        Optional<Book> book = bookRepo.findById(id);
        if(book.isPresent())
            return book;
        throw new KunduException(Services.LIBRARY_SERVICE.name(),"Book with id: '"+id+"' not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public boolean verifyQuizUser(String code, Integer userId) throws IOException {
        var member = memberRepo.findByPerson(Person.builder().id(userId).build());
        var session = sessionRepo.findByLessonCode(code);
        if(member.isEmpty() || session==null)
            throw new KunduException(Services.LIBRARY_SERVICE.name(),"Member or lesson doesn't exists",HttpStatus.NOT_FOUND);
        var assist = assistRepo.findByMemberAndSession(member.get(),session);
        if (null!=assist && assist.isQuiz())
            throw new KunduException(Services.LIBRARY_SERVICE.name(),"Quiz already performed",HttpStatus.NOT_FOUND);
        return true;
    }
}
