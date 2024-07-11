package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Group;
import com.citse.kunduApp.entity.Session;
import com.citse.kunduApp.utils.models.GroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Optional<Group> getById(int id);
    Group save(Group group);
    void delete(int groupId);

    //filters
    Object getByCode(String code);
    Group applyJoinGroup(String code, String kunduCode);
    List<GroupDTO> getGroupPages(Pageable pageable);
    List<Session> getSessionFromGroupByCode(String code);
    List<GroupDTO> getRankingOfGeneralGroups(int limit);
    List<?> getMembersByGroupCode(String code);
}
