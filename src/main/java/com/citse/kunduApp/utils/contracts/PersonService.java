package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Person;

import java.util.List;

public interface PersonService {
    List<Person> getAll();
    Person getById(int id);
    Person save(Person person);
    void delete(int personId);

    //FILTERS
    Person findByKunduCode(String kunduCode);
}
