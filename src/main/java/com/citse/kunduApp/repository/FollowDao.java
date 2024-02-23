package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Follow;
import com.citse.kunduApp.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowDao extends JpaRepository<Follow,Integer> {
    Page<Follow> findByFollower(Person follower, Pageable pageable);
    Page<Follow> findByFollowed(Person followed, Pageable pageable);
    Follow findByFollowerAndFollowed(Person follower, Person followed);
}
