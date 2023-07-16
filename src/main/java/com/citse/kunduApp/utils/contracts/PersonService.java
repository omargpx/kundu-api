package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    List<Person> getAll();
    Person getById(int id);
    Person save(Person person);
    void delete(int personId);

    //FILTERS
    Person findByKunduCode(String kunduCode);
    Page<Person> getPeoplePages(Pageable pageable);
}
