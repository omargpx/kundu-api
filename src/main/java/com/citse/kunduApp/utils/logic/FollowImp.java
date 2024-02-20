package com.citse.kunduApp.utils.logic;

import com.citse.kunduApp.entity.Follow;
import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.FollowDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.contracts.FollowService;
import com.citse.kunduApp.utils.models.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowImp implements FollowService {

    @Autowired
    private FollowDao repo;
    @Autowired
    private PersonDao personDao;


    @Override
    public Follow save(int  from, int to) {
        Person follower = personDao.findById(from).orElseThrow(() ->
          new KunduException(Services.FOLLOW_SERVICE.name(),"follow person id not found", HttpStatus.NOT_FOUND));
        Person followed = personDao.findById(to).orElseThrow(() ->
          new KunduException(Services.FOLLOW_SERVICE.name(),"followed person id not found", HttpStatus.NOT_FOUND));
        Follow log = Follow.builder().follower(follower).followed(followed).build();
        return repo.save(log);
    }
}
