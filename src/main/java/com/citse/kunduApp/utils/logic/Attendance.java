package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.*;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.*;
import com.citse.kunduApp.utils.contracts.UQRService;
import com.citse.kunduApp.utils.models.Services;
import com.citse.kunduApp.utils.models.SimpleMember;
import com.citse.kunduApp.utils.models.SimplePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class Attendance implements UQRService {

    @Autowired
    private UQRDao repo;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private AssistDao assistDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private GroupDao groupDao;


    @Override
    public UserQuizResult getById(int id) {
        UserQuizResult uqr = repo.findById(id).orElseThrow(()->
          new KunduException(Services.USER_QUIZ_RESULTS.name(),"Not found user results", HttpStatus.NOT_FOUND));
        assert null!=uqr;
        return uqr;
    }
    @Override
    public List<Member> getAllByGroup(int groupId) {
        List<Member> members = memberDao.findAllByGroup(Group.builder().id(groupId).build());
        if(!members.isEmpty())
            return members;
        throw new KunduException(Services.USER_QUIZ_RESULTS.name(),"Not found user results by group", HttpStatus.NOT_FOUND);
    }

    @Override
    public Assist save(UserQuizResult uqr, int memberId, int sessionId) {
        Member member = memberDao.findById(memberId).orElseThrow();
        Group group = groupDao.findById(member.getGroup().getId()).orElseThrow(()->
          new KunduException(Services.ATTENDANCE_SERVICE.name()," group not found", HttpStatus.NOT_FOUND));
        Session session = Session.builder().id(sessionId).build();
        Assist verifyAssist = assistDao.findByMemberAndSession(member, session);
        uqr.setMemberResult(member);

        if (null != verifyAssist) {
            updateExperienceAndQuizStatus(verifyAssist, uqr.getXp(),group, uqr.getPoints());
            repo.save(uqr);
            return assistDao.findById(verifyAssist.getId()).orElseThrow();
        }
        //create row and save
        repo.save(uqr);
        Assist assist = Assist.builder()
          .date(LocalDate.now())
          .attendance(false)
          .quiz(true)
          .member(member)
          .session(session)
          .build();
        var assistLog = assistDao.save(assist);
        updateExperienceForMember(member, uqr.getXp());
        return assistLog;
    }

    @Override
    public Assist saveQRAssist(int sessionId, int memberId) {
        Member member = Member.builder().id(memberId).build();
        Session session = Session.builder().id(sessionId).build();
        Assist verifyAssist = assistDao.findByMemberAndSession(member, session);

        if (null!=verifyAssist){
            verifyAssist.setAttendance(true);
            return assistDao.save(verifyAssist);
        }
        //create row and save
        Assist assist = Assist.builder()
          .date(LocalDate.now())
          .attendance(true)
          .quiz(false)
          .member(member)
          .session(session)
          .build();
        return assistDao.save(assist);
    }

    private void updateExperienceAndQuizStatus(Assist assist, int xp, Group group, int points) {
        Person person = personDao.findByMember(assist.getMember())
          .orElseThrow(() -> new KunduException(Services.USER_QUIZ_RESULTS.name(), "Error finding person by member", HttpStatus.NOT_FOUND));
        person.setExperience(person.getExperience() + xp);
        assist.setQuiz(true);
        group.setPoints(group.getPoints() + points);
        groupDao.save(group);
        personDao.save(person);
        assistDao.save(assist);
    }

    private void updateExperienceForMember(Member member, int xp) {
        Optional<Person> person = personDao.findByMember(member);
        if (person.isEmpty())
            throw new KunduException(Services.USER_QUIZ_RESULTS.name(),"Error to find person by member", HttpStatus.NOT_FOUND);
        person.get().setExperience(person.get().getExperience() + xp);
        personDao.save(person.get());
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
