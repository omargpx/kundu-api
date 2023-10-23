package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.*;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.ListenerDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.SpaceDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.utils.models.Services;
import io.agora.media.RtcTokenBuilder2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Space Service Class.
 * To create Space-streaming audio and view details
 * connected to agora.io
 */
@Service
@RequiredArgsConstructor
public class SpaceService {
    //region attributes
    private final UserDao userRepo;
    private final SpaceDao spaceRepo;
    private final PersonDao personRepo;
    private final KunduUtilitiesService kus;
    private final ListenerDao listenerRepo;
    private static final String appId = "b8dcc3cc668946359839f718fa335508";// omar - 3fb16d53c7f441d6ba2231a860e1cbc5
    private static final String appCertificate = "92e33779cc404513872c91dc3fd451d9"; //omar - 2cf779073aa048ff8e5192ff2010eec6
    //endregion

    @Transactional
    public Space create(Space space, Integer userId) {
        validateUser(userId);
        var spaceSave = Space.builder()
          .name(space.getName())
          .code(kus.SecureCode("KS"))
          .creation(LocalDateTime.now())
          .token(generateTokenRoom())
          .moderator(User.builder().id(userId).build())
          .build();
        return spaceRepo.save(spaceSave);
    }

    public void applyJoin(Space space, User user, String uuId) {
        var subscriber = createListener(space, user, uuId);
        listenerRepo.save(subscriber);
    }

    public Space closeSpace(Integer id) {
        Space space = getSpaceById(id);
        space.setStatus(false);//space closed
        return spaceRepo.save(space);
    }

    public Space getSpaceById(Integer id) {
        return spaceRepo.findById(id)
          .orElseThrow(() -> new KunduException(Services.SPACE_SERVICE.name(), "space not found", HttpStatus.NOT_FOUND));
    }

    public List<Space> historySpacesFromUser(int userId) {
        validateUser(userId);
        return spaceRepo.findAllByStatusIsFalseAndModerator(User.builder().id(userId).build());
    }

    public List<Space> getSpacesAround(int userId) {
        validateUser(userId);
        List<Space> recommendSpaces = new ArrayList<>(findFriendsSpaces(userId));
        if (recommendSpaces.isEmpty()) {
            List<Space> randomSpaces = getListSpaces(0, 7);
            recommendSpaces = filterAndAddSpaces(recommendSpaces, randomSpaces);
        }
        List<Space> additionalSpaces = getListSpaces(0, 3);
        recommendSpaces = filterAndAddSpaces(recommendSpaces, additionalSpaces);
        return recommendSpaces;
    }
    public List<Space> getListSpaces(int page, int size) {
        Pageable order = PageRequest.of(page, size);
        List<Space> spaces = spaceRepo.findRandomSpacesWithStatusTrue(order);
        return spaces.stream().distinct().collect(Collectors.toList());
    }
    private List<Space> filterAndAddSpaces(List<Space> recommendSpaces, List<Space> spacesToAdd) {
        Set<Space> spaceSet = new HashSet<>(recommendSpaces); // convert to set to delete the duplicates
        spaceSet.addAll(spacesToAdd);
        return new ArrayList<>(spaceSet);
    }
    /* Filter friend spaces
     * only return spaces that nonNull objects
     * */
    private List<Space> findFriendsSpaces(Integer userId) {
        // explicitly load collection
        List<Person> following = personRepo.findPersonWithFollowingById(userId);
        return following.stream()
          .flatMap(person -> person.getFollowing().stream())
          .map(Follow::getFollowed)
          .map(friend -> spaceRepo.findByStatusIsTrueAndModerator(User.builder().id(friend.getId()).build()))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
    private void validateUser(int userId) {
        userRepo.findById(userId)
          .orElseThrow(() -> new KunduException(Services.SPACE_SERVICE.name(), "User not found", HttpStatus.NOT_FOUND));
    }
    private Listener createListener(Space space, User user, String uuId) {
        return Listener.builder()
          .space(space)
          .uuid(uuId)
          .userSpace(user)
          .build();
    }
    private String generateTokenRoom() {
        RtcTokenBuilder2 token = new RtcTokenBuilder2();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + 3600);
        String codeRoom = kus.SecureCode("KS");
        return token.buildTokenWithUid(appId, appCertificate, codeRoom, 0,
          RtcTokenBuilder2.Role.ROLE_PUBLISHER, timestamp, timestamp);
    }

}
