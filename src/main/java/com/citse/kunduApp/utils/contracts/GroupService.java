package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Member;
import com.citse.kunduApp.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> getAll();
    Optional<Group> getById(int id);
    Group save(Group group);
    void delete(int groupId);

    //filters
    Group getByCode(String code);
    Group applyJoinGroup(String code, String kunduCode);
    Page<Group> getGroupPages(Pageable pageable);
    List<Session> getSessionFromGroupByCode(String code);
    void updateSession(String code, String codeLesson);

    List<Member> getMembersByGroupCode(String code);
}
