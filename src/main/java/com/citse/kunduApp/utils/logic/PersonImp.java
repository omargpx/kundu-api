package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Follow;
import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.FollowDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.security.mock.Invitation;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.models.FollowerDTO;
import com.citse.kunduApp.utils.models.Services;
import com.citse.kunduApp.utils.models.SimplePerson;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PersonImp implements PersonService {

    @Autowired
    private PersonDao repo;
    @Autowired
    private UserDao userRepo;
    @Autowired
    private FollowDao followRepo;

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
        person.setNumFollowers(person.getFollowers().size());
        person.setNumFollowing(person.getFollowing().size());
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
    @Transactional
    public Person update(int id, Person p) {
        Person person = getById(id);
        if (!Hibernate.isInitialized(person.getUserDetail().getInvitations())) {
            Hibernate.initialize(person.getUserDetail().getInvitations());
        }
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
        person.setNumFollowers(person.getFollowers().size());
        person.setNumFollowing(person.getFollowing().size());
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

    @Override
    public Page<Person> getPeoplePages(Pageable pageable) {//TODO:map to dto
        return repo.findAllBy(pageable);
    }

    @Override
    public Person findByPhone(String phone) {
        Optional<Person> person = repo.findByPhone(phone);
        if(person.isPresent())
            return person.get();
        throw new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public List<SimplePerson> searchPerson(String query) {//TODO: load only necessary data...
        Pageable pageable = PageRequest.of(0, 5);
        List<Person> matchingUsers = repo.searchByFullNameOrNickname(query,pageable);
        return matchingUsers.stream().map(this::personToDTO).toList();
    }

    @Override
    @Transactional
    public List<FollowerDTO> getFollowers(int id,Integer page,Integer size) {
        Person person = repo.findById(id)
                .orElseThrow(() -> new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(page, Objects.requireNonNullElse(size,100));
        Page<Follow> followersPage = followRepo.findByFollowed(person, pageable);

        return followersPage.getContent()
                .stream().map(this::followerMapToDTO).toList();
    }

    @Override
    @Transactional
    public List<?> getFollowings(int id, Integer page, Integer size) {
        Person person = repo.findById(id)
                .orElseThrow(() -> new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(page, Objects.requireNonNullElse(size,100));
        Page<Follow> followersPage = followRepo.findByFollower(person, pageable);

        return followersPage.getContent()
                .stream().map(this::followedMapToDTO).toList();
    }

    @Override
    @Transactional
    public List<?> getSuggestFriends(Integer id) {
        repo.findById(id)
                .orElseThrow(() -> new KunduException(Services.PERSON_SERVICE.name(), "Person not found", HttpStatus.NOT_FOUND));
        Pageable pageable = PageRequest.of(0, 7);
        List<Person> suggestedFriends = repo.getSuggestFriends(id, pageable);

        return suggestedFriends.stream().map(this::personToDTO).toList();
    }

    private FollowerDTO followerMapToDTO(Follow follow){
        FollowerDTO dto = new FollowerDTO();
        dto.setId(follow.getId());
        dto.setDate(follow.getDate());
        dto.setPerson(personToDTO(follow.getFollower()));
        return dto;
    }
    private FollowerDTO followedMapToDTO(Follow follow){
        FollowerDTO dto = new FollowerDTO();
        dto.setId(follow.getId());
        dto.setDate(follow.getDate());
        dto.setPerson(personToDTO(follow.getFollowed()));
        return dto;
    }
    private SimplePerson personToDTO(Person person){
        return SimplePerson.builder()
                .id(person.getId())
                .avatar(person.getAvatar())
                .birth(person.getBirth())
                .biography(person.getBiography())
                .joinDate(person.getJoinDate())
                .experience(person.getExperience())
                .phone(person.getPhone())
                .kunduCode(person.getKunduCode())
                .name(person.getName())
                .user(person.getUserDetail())
                .build();
    }
}
