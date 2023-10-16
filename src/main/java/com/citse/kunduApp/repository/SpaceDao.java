package com.citse.kunduApp.repository;

import com.citse.kunduApp.entity.Space;
import com.citse.kunduApp.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceDao extends JpaRepository<Space,Integer> {
    Optional<Space> findByCode(String code);
    List<Space> findAllByStatusIsFalseAndModerator(User moderator);
    Space findByStatusIsTrueAndModerator(User moderator);
    @Query("SELECT s FROM Space s WHERE s.status = true ORDER BY FUNCTION('RAND')")
    List<Space> findRandomSpacesWithStatusTrue(Pageable pageable);
}
