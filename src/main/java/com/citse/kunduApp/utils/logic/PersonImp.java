package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.models.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonImp implements PersonService {

    @Autowired
    private PersonDao repo;

    @Override
    public List<Person> getAll() {
        List<Person> personList = repo.findAll();
        if(!personList.isEmpty())
            return personList;
        throw new KunduException(Services.PERSON_SERVICE.name(), "PersonList is empty", HttpStatus.NO_CONTENT);
    }

    @Override
    public Person getById(int id) {
        Person person = repo.findById(id).orElse(null);
        if(null!=person)
            return person;
        throw new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public Person save(Person person) {
        return repo.save(person);
    }

    @Override
    public void delete(int personId) {
        repo.deleteById(personId);
    }

    @Cacheable(value = "findByKunduCode")
    @Override
    public Person findByKunduCode(String kunduCode) {
        Person person = repo.findByKunduCode(kunduCode);
        if(null!=person)
            return person;
        throw new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND);
    }

    @Cacheable(value = "getPeopleByPage")
    @Override
    public Page<Person> getPeoplePages(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
