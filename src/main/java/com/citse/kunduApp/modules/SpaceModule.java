package com.citse.kunduApp.modules;

import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.GroupDao;
import com.citse.kunduApp.repository.SpaceDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.repository.UserDao;
import com.citse.kunduApp.utils.logic.SpaceService;
import com.citse.kunduApp.utils.models.*;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
//@RequiredArgsConstructor
public class SpaceModule {
    private final SocketSpaceService socketService;
    @Autowired
    private SpaceService service;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private SpaceDao spaceRepo;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    public SpaceModule(SocketIOServer server, SocketSpaceService socketService) {
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("joinSpace", RequestSpace.class, onConnectSpace());
        server.addEventListener("leftSpace", RequestSpace.class, onDisconnectedSpace());
        server.addEventListener("sendSpaceMessage", POST.class, onChatSpace());
        server.addEventListener("sendChannelMessage", POST.class, onChatChannel());
        server.addEventListener("sleepUser", SleepSocket.class, sleepConnection());
        server.addEventListener("unSleepUser", SleepSocket.class,unSleepConnection());
        //audio-event
    }

    private DataListener<SleepSocket> unSleepConnection() {
        return (client, data, ackSender) -> {
            var person = personDao.findPersonByUsername(data.getUser());
            var group = groupDao.findByCode(data.getGroup());
            if (person==null || group==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "socket sleep error", HttpStatus.NOT_FOUND);
            socketService.onConnect(data.getGroup(), client, data.getUser());
            changeUserStatus(data.getUser(), true);//change user status (online)
        };
    }

    private DataListener<SleepSocket> sleepConnection() {
        return (client, data, ackSender) -> {
            var person = personDao.findPersonByUsername(data.getUser());
            var group = groupDao.findByCode(data.getGroup());
            if (person==null || group==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "socket sleep error", HttpStatus.NOT_FOUND);
            socketService.onDisconnect(data.getGroup(), client, data.getUser());
            changeUserStatus(data.getUser(), false);//change user status (offline)
        };
    }

    private DataListener<POST> onChatChannel() {
        return (client, data, ackSender)->{
            socketService.sendChannelMessage(data.getRoom(), client, data.getMessage(), data.getUsername());
        };
    }

    private DataListener<POST> onChatSpace() {
        return (client, data, ackSender)->{
            socketService.sendSpaceMessage(data.getRoom(), client, data.getMessage(), data.getUsername());
        };
    }

    private DataListener<RequestSpace> onDisconnectedSpace() {
        return (client, data, ackSender) -> {
            var person = personDao.findPersonByUsername(data.getUsername());
            var space = spaceRepo.findByCode(data.getSpace());
            if (space.isEmpty() || person==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "sockets space disconnect error", HttpStatus.NOT_FOUND);
            var user_space = UserSubListener.builder()
              .username(person.getUserDetail().getUsername())
              .avatar(person.getAvatar())
              .uuid(data.getUidAgora())
              .build();
            socketService.leftSpace(space.get(),client,user_space);
            client.leaveRoom(data.getSpace());
            log.info("Socket ID[{}]  Disconnect to space", client.getSessionId().toString());
        };
    }

    private DataListener<RequestSpace> onConnectSpace() {
        return (client, data, ackSender) -> {
            var person = personDao.findPersonByUsername(data.getUsername());
            var space = spaceRepo.findByCode(data.getSpace());
            if (space.isEmpty() || person==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "sockets space connect error", HttpStatus.NOT_FOUND);
            var user_space = UserSubListener.builder()
              .username(person.getUserDetail().getUsername())
              .avatar(person.getAvatar())
              .uuid(data.getUidAgora())
              .build();
            socketService.joinSpace(space.get(),client,user_space);
            client.joinRoom(data.getSpace());
            log.info("Socket ID[{}]  Connected to space", client.getSessionId().toString());
            //service.applyJoin(space.get(), person.getUserDetail(), data.getUidAgora());
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String groupCode, username;
            username = params.get("username").get(0);
            groupCode = params.get("group").get(0);
            //required att
            var person = personDao.findPersonByUsername(username);
            var group = groupDao.findByCode(groupCode);
            if (person==null || group==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "socket connect error", HttpStatus.NOT_FOUND);
            socketService.onConnect(groupCode,client,username);
            client.joinRoom(groupCode);
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
            //changeUserStatus(username,true);//change user status (online)
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String groupCode, username;
            username = params.get("username").get(0);
            groupCode = params.get("group").get(0);
            var person = personDao.findPersonByUsername(username);
            var group = groupDao.findByCode(groupCode);
            if (person==null || group==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "socket disconnect error", HttpStatus.NOT_FOUND);
            socketService.onDisconnect(groupCode,client,username);
            client.leaveRoom(groupCode);
            log.info("Socket ID[{}]  Disconnected to socket", client.getSessionId().toString());
            //changeUserStatus(username,false);//change user status (offline)
        };
    }

    private void changeUserStatus(String username,boolean status){
        User user = userDao.findByUsername(username).orElseThrow();
        user.setIsConnect(status);
        userDao.save(user);
    }
}
