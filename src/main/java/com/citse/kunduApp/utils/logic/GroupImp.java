package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.GroupDao;
import com.citse.kunduApp.repository.MemberDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.contracts.GroupService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.models.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GroupImp implements GroupService {

    @Autowired
    private GroupDao repo;
    @Autowired
    private KunduUtilitiesService kus;
    @Autowired
    private PersonDao personRepo;
    @Autowired
    private MemberDao memberRepo;

    @Override
    public List<Group> getAll() {
        List<Group> groups = repo.findAll();
        if(!groups.isEmpty())
            return groups;
        throw new KunduException(Services.GROUP_SERVICE.name(),"Groups is empty", HttpStatus.NO_CONTENT);
    }

    @Override
    public Optional<Group> getById(int id) {
        Optional<Group> group = repo.findById(id);
        if(group.isPresent())
            return group;
        throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public Group save(Group group) {
        group.setCode(kus.SecureCode("KSG"));
        group.setCreation(LocalDate.now());
        group.setPoints(0);
        group.setLevel(1);
        return repo.save(group);
    }

    @Override
    public void delete(int groupId) {
        repo.deleteById(groupId);
    }

    @Override
    public Group getByCode(String code) {
        Group group = repo.findByCode(code);
        if (null!= group)
            return group;
        throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public Group applyJoinGroup(String code, String kunduCode) {
        Person person = personRepo.findByKunduCode(kunduCode);
        Group group = getByCode(code);
        if(group==null || person==null)
            throw new KunduException(Services.GROUP_SERVICE.name(),"the code is wrong", HttpStatus.NOT_FOUND);

        memberRepo.save(Member.builder().
                person(person).
                group(group).
                dateJoin(LocalDate.now())
                .build());
        return group;
    }
}
