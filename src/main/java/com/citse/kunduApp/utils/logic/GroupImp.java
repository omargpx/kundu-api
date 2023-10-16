package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.entity.Session;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.GroupDao;
import com.citse.kunduApp.repository.MemberDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.SessionDao;
import com.citse.kunduApp.utils.contracts.GroupService;
import com.citse.kunduApp.utils.contracts.KunduUtilitiesService;
import com.citse.kunduApp.utils.contracts.ThemesContentService;
import com.citse.kunduApp.utils.models.GroupDTO;
import com.citse.kunduApp.utils.models.Services;
import com.citse.kunduApp.utils.models.SimpleMember;
import com.citse.kunduApp.utils.models.SimplePerson;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ThemesContentService library;
    @Autowired
    private SessionDao sessionRepo;

    @Override
    public List<Group> getAll() {
        List<Group> groups = repo.findAll();
        if(!groups.isEmpty())
            return groups;
        throw new KunduException(Services.GROUP_SERVICE.name(),"Groups is empty", HttpStatus.NO_CONTENT);
    }

    @Override
    @Transactional
    public Optional<Group> getById(int id) {
        Optional<Group> group = repo.findById(id);
        if(group.isEmpty())
            throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
        if(!Hibernate.isInitialized(group.get().getMembers()))
            Hibernate.initialize(group.get().getMembers());
        return group;
    }

    @Override
    public Group save(Group group) {
        group.setCode(kus.SecureCode("KSG"));
        group.setCreation(LocalDate.now());
        group.setPhase(group.getPhase() != null ? group.getPhase() : 4);
        group.setPoints(0);
        group.setLevel(1);
        var g = repo.save(group);
        library.subscribe(group.getPhase(), g);
        return g;
    }

    @Override
    public void delete(int groupId) {
        repo.deleteById(groupId);
    }

    @Override
    @Transactional
    public Object getByCode(String code) {
        Group group = repo.findByCode(code);
        if (null== group)
            throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
        //initialize entities
        Hibernate.initialize(group.getMembers());
        List<SimpleMember> members = group.getMembers().stream().map(this::memberToDTO).toList();
        var gr = groupToDTO(group);
        gr.setMembers(members);
        return gr;
    }

    @Override
    @Transactional
    public Group applyJoinGroup(String code, String kunduCode) {
        Person person = personRepo.findByKunduCode(kunduCode);
        Group group = repo.findByCode(code);
        if(group==null || person==null)
            throw new KunduException(Services.GROUP_SERVICE.name(),"the code is wrong", HttpStatus.NOT_FOUND);
        if (group.getMembers().size()>=45)
            throw new KunduException(Services.GROUP_SERVICE.name(),"Limit reached", HttpStatus.NOT_FOUND);
        var verifyMember = memberRepo.findByPerson(person);
        if(verifyMember.isPresent())
            throw new KunduException(Services.GROUP_SERVICE.name(),"Member already exists", HttpStatus.BAD_REQUEST);
        memberRepo.save(Member.builder().
                person(person).
                group(group).
                dateJoin(LocalDate.now())
                .build());
        return group;
    }

    @Cacheable(value = "getGroupByPage")
    @Override
    public Page<Group> getGroupPages(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Transactional
    public List<Session> getSessionFromGroupByCode(String code) {
        var group = repo.findByCode(code);
        if (null== group)
            throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
        List<Session> sessions = repo.getSessionsByGroupCode(group);
        if(sessions.isEmpty())
            throw new KunduException(Services.GROUP_SERVICE.name(), "There are no sessions in this group", HttpStatus.NO_CONTENT);
        Session lastSession = sessions.get(sessions.size()-1);
        if(LocalDate.now().equals(lastSession.getExecution().plusDays(1))) {
            repo.cleanSessions(group);
            library.subscribe(group.getPhase(),group);
            return repo.getSessionsByGroupCode(group);
        }
        return sessions;
    }

    @Override
    @Transactional
    public void updateSession(String code, String codeLesson) {//TODO: auto activate next lesson
        var group = repo.findByCode(code);
        if (null== group)
            throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
        Session session = sessionRepo.findByLessonCode(codeLesson);
        if(session==null)
            throw new KunduException(Services.GROUP_SERVICE.name(), "Unregistered lesson", HttpStatus.NOT_FOUND);
        if(!group.getSessions().contains(session))
            throw new KunduException(Services.GROUP_SERVICE.name(), "The lesson code was not found with the "+group.getName()+" sessions.", HttpStatus.NOT_FOUND);
        var position = group.getSessions().indexOf(session);
        session.setStatus(2);
        sessionRepo.save(session);
        if(position>=0 && position< group.getSessions().size()){
            var nextSessionOn = group.getSessions().get(position+1);
            nextSessionOn.setStatus(1);
            group.getSessions().set((position+1),nextSessionOn);
        }
    }

    @Override
    @Transactional
    public List<?> getMembersByGroupCode(String code) {
        var group = repo.findByCode(code);
        if (null== group)
            throw new KunduException(Services.GROUP_SERVICE.name(),"Group not found", HttpStatus.NOT_FOUND);
        return repo.getMembersByGroup(group).stream().map(this::memberToDTO).toList();
    }

    private GroupDTO groupToDTO(Group group){
        GroupDTO g = new GroupDTO();
        g.setId(group.getId());
        g.setName(group.getName());
        g.setImage(group.getImage());
        g.setLema(group.getLema());
        g.setEntity(group.getGEntity());
        g.setCode(group.getCode());
        g.setSong(group.getSong());
        g.setTag(group.getTag());
        g.setPhase(group.getPhase());
        g.setLevel(group.getLevel());
        g.setPoints(group.getPoints());
        g.setVerse(group.getVerse());
        g.setCreation(group.getCreation());
        return g;
    }
    private SimpleMember memberToDTO(Member member){
        SimpleMember sm = new SimpleMember();
        sm.setId(member.getId());
        sm.setDateJoin(member.getDateJoin());
        sm.setPerson(personToDTO(member.getPerson()));
        return sm;
    }
    private SimplePerson personToDTO(Person person){
        return SimplePerson.builder()
                .id(person.getId())
                .avatar(person.getAvatar())
                .biography(person.getBiography())
                .kunduCode(person.getKunduCode())
                .name(person.getName())
                .user(person.getUserDetail())
                .build();
    }
}
