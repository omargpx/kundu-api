package com.citse.kunduApp.modules;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Session;
import com.citse.kunduApp.repository.GroupDao;
import com.citse.kunduApp.repository.SessionDao;
import com.citse.kunduApp.utils.contracts.ThemesContentService;
import com.citse.kunduApp.utils.models.SessionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Cacheable
@Component
@RequiredArgsConstructor
public class TaskScheduler {

    private final GroupDao groupRepo;
    private final SessionDao sessionRepo;
    private final ThemesContentService library;

    @Scheduled(cron = "0 0 1 ? * SAT") // execute this method on Saturdays at 1:00 am
    @Transactional
    public void changeSessionStatus() {
        List<Group> groups = groupRepo.findAll();
        groups.forEach(this::processGroupSessions);
    }

    private void processGroupSessions(Group group) {
        List<Session> sessions = group.getSessions();
        Optional<Session> lastActiveSession = sessions.stream()
          .filter(s -> s.getStatus() == 1)
          .findFirst();
        var lastPosition= sessions.size()-1;

        lastActiveSession.ifPresent(session -> {
            int position = sessions.indexOf(session);
            if (position==lastPosition){
                groupRepo.cleanSessions(group);
                library.subscribe(group.getPhase(),group);
            }else {
                updateSessionStatus(session, SessionStatus.CLOSED.getValue());
                activateNextSession(sessions, position);
            }
        });
    }

    private void updateSessionStatus(Session session, int status) {
        session.setStatus(status);
        sessionRepo.save(session);
    }

    private void activateNextSession(List<Session> sessions, int position) {
        if (position >= 0 && position < sessions.size() - 1) {
            Session nextSession = sessions.get(position + 1);
            updateSessionStatus(nextSession, SessionStatus.ON.getValue());
        }
    }

}
