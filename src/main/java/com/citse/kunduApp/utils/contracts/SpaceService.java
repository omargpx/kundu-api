package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.*;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.SpaceDao;
import com.citse.kunduApp.repository.ListenerDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.utils.models.Services;
import io.agora.media.RtcTokenBuilder2;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Space create(Space space, Integer userId){
        var user = userRepo.findById(userId).orElse(null);
        if(user==null)
            throw new RuntimeException("User not found");

        RtcTokenBuilder2 token = new RtcTokenBuilder2();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + 3600);
        String codeRoom = kus.SecureCode("KS");
        String tokenRoom = token.buildTokenWithUid(appId,appCertificate,codeRoom,0,
                RtcTokenBuilder2.Role.ROLE_PUBLISHER,timestamp,timestamp);
        var spaceSave = Space.builder()
                .name(space.getName())
                .code(codeRoom)
                .creation(LocalDateTime.now())
                .token(tokenRoom)
                .moderator(user)
                .build();
        return spaceRepo.save(spaceSave);
    }

    public void applyJoin(Space space, User user, String uuId){
        var subscriber = Listener.builder()
                .space(space)
                .uuid(uuId)
                .userSpace(user)
                .build();
        listenerRepo.save(subscriber);
    }

    public Space closeSpace(Integer id) {
        Space space = getSpaceById(id);
        space.setStatus(false);//space closed
        return spaceRepo.save(space);
    }

    public Space getSpaceById(Integer id){
        Optional<Space> space = spaceRepo.findById(id);
        if(space.isEmpty())
            throw new KunduException(Services.SPACE_SERVICE.name(), "space not found", HttpStatus.NOT_FOUND);
        return space.get();
    }

    public List<Space> historySpacesFromUser(int userId){
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty())
            throw new KunduException(Services.SPACE_SERVICE.name(), "user not found", HttpStatus.NOT_FOUND);
        return spaceRepo.findAllByStatusIsFalseAndModerator(user.get());
    }

    public List<Space> getSpacesAround(int userId){
        userRepo.findById(userId).orElseThrow();
        List<Space> recommendSpaces = new ArrayList<>(findFriendsSpaces(userId));
        if(recommendSpaces.isEmpty())
             recommendSpaces.addAll(getListSpaces(0,7));
        recommendSpaces.addAll(getListSpaces(0,3));
        return recommendSpaces;
    }

    /* Filter friend spaces
    * only return spaces that nonNull objects
    * */
    private List<Space> findFriendsSpaces(Integer userId){
        Person me = personRepo.findById(userId).orElseThrow();
        return me.getFollowing().stream()
                .map(Follow::getFollowed)
                .map(friend -> spaceRepo.findByStatusIsTrueAndModerator(User.builder().id(friend.getId()).build()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Space> getListSpaces(int page, int size){
        Pageable order = PageRequest.of(page, size);
        return spaceRepo.findRandomSpacesWithStatusTrue(order);
    }

}
