package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Group;

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
}
