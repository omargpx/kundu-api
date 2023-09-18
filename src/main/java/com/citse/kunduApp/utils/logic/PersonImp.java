package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.security.mock.Invitation;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.models.Services;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonImp implements PersonService {

    @Autowired
    private PersonDao repo;
    @Autowired
    private UserDao userRepo;

    @Override
    public List<Person> getAll() {
        List<Person> personList = repo.findAll();
        if(!personList.isEmpty())
            return personList;
        throw new KunduException(Services.PERSON_SERVICE.name(), "PersonList is empty", HttpStatus.NO_CONTENT);
    }

    @Override
    @Transactional
    public Person getById(int id) {
        Person person = repo.findById(id)
                .orElseThrow(() -> new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND));

        if (!Hibernate.isInitialized(person.getUserDetail().getInvitations())) {
            Hibernate.initialize(person.getUserDetail().getInvitations());
        }

        List<Object> guests = new ArrayList<>();

        if (person.getUserDetail() != null && person.getUserDetail().getInvitations() != null) {
            List<Invitation> invitations = person.getUserDetail().getInvitations();
            invitations.forEach(invitation -> {
                Optional<User> userOP = userRepo.findByEmail(invitation.getEmail());
                if (userOP.isPresent()) {
                    User user = userOP.get();
                    Person p = repo.findPersonByUsername(user.getUsername());
                    guests.add(p);
                }
            });
            Objects.requireNonNull(person.getUserDetail()).setGuests(guests);
        }
        return person;
    }
    @Override
    public Person save(Person person) {
        return repo.save(person);
    }

    @Override
    public Person update(int id, Person p) {
        Person person = getById(id);
        person.setBiography(p.getBiography() != null? p.getBiography() : person.getBiography());
        person.setBirth(p.getBirth() != null? p.getBirth() : person.getBirth());
        person.setAvatar(p.getAvatar() != null? p.getAvatar() : person.getAvatar());
        person.setName(p.getName() != null? p.getName() : person.getName());
        person.setPhone(p.getPhone() != null ? p.getPhone() : person.getPhone());
        return repo.save(person);
    }

    @Override
    public void delete(int personId) {
        repo.deleteById(personId);
    }

    @Cacheable(value = "findByKunduCode")
    @Override
    @Transactional
    public Person findByKunduCode(String kunduCode) {
        Person person = repo.findByKunduCode(kunduCode);
        if(null==person)
            throw new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND);
        if (!Hibernate.isInitialized(person.getUserDetail().getInvitations())) {
            Hibernate.initialize(person.getUserDetail().getInvitations());
        }

        List<Object> guests = new ArrayList<>();

        if (person.getUserDetail() != null && person.getUserDetail().getInvitations() != null) {
            List<Invitation> invitations = person.getUserDetail().getInvitations();
            invitations.forEach(invitation -> {
                Optional<User> userOP = userRepo.findByEmail(invitation.getEmail());
                if(userOP.isPresent()){
                    User user = userOP.get();
                    Person p = repo.findPersonByUsername(user.getUsername());
                    guests.add(p);
                }
            });
            Objects.requireNonNull(person.getUserDetail()).setGuests(guests);
        }
        return person;
    }

    @Cacheable(value = "getPeopleByPage")
    @Override
    public Page<Person> getPeoplePages(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Person findByPhone(String phone) {
        Optional<Person> person = repo.findByPhone(phone);
        if(person.isPresent())
            return person.get();
        throw new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND);
    }

    @Cacheable(value = "searchPersonByFullNameOrNickname")
    @Override
    public List<Person> searchPerson(String query) {
        List<Person> matchingUsers = repo.searchByFullNameOrNickname(query);
        if(!matchingUsers.isEmpty())
            return matchingUsers;
        return new ArrayList<>();
    }
}
