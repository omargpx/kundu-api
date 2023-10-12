package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Listener;
import com.citse.kunduApp.entity.Space;
import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.repository.SpaceDao;
import com.citse.kunduApp.repository.ListenerDao;
import com.citse.kunduApp.repository.UserDao;
import io.agora.media.RtcTokenBuilder2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceService {

    //region attributes
    private final UserDao userRepo;
    private final SpaceDao spaceRepo;
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
                .creation(LocalDate.now())
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

    @Transactional
    public List<Space> getSpaces(){
        return spaceRepo.findAll();
    }

}
