package com.citse.kunduApp.modules;

import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.SpaceDao;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.contracts.SocketService;
import com.citse.kunduApp.utils.contracts.SpaceService;
import com.citse.kunduApp.utils.models.POST;
import com.citse.kunduApp.utils.models.Services;
import com.citse.kunduApp.utils.models.UserSubListener;
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
    private final SocketService socketService;
    @Autowired
    private SpaceService service;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private SpaceDao spaceRepo;

    public SpaceModule(SocketIOServer server, SocketService socketService) {
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", POST.class, onChatReceived());
    }

    private DataListener<POST> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessage(data.getRoom(), senderClient, data.getMessage(), data.getUsername());
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String room_code, username, uuid;
            username = params.get("username").get(0);
            room_code = params.get("room").get(0);
            uuid = params.get("uuid").get(0);
            //required attributes
            var person = personDao.findPersonByUsername(username);
            var space = spaceRepo.findByCode(room_code);
            if (space.isEmpty() || person==null)
                throw new KunduException(Services.SPACE_SERVICE.name(), "sockets space connect error", HttpStatus.NOT_FOUND);
            var user_space = UserSubListener.builder()
                    .username(person.getUserDetail().getUsername())
                    .avatar(person.getAvatar())
                    .uuid(uuid)
                    .build();
            //add the client to the room
            client.joinRoom(room_code);
            socketService.join(space.get(), client, user_space);
            service.applyJoin(space.get(), person.getUserDetail(), uuid);

            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
            String username = params.get("username").get(0);
            String room_code = params.get("room").get(0);
            String uuid = params.get("uuid").get(0);
            //required attributes
            var person = personDao.findPersonByUsername(username);
            var space = spaceRepo.findByCode(room_code);
            if (space.isEmpty() || person==null)
                throw new KunduException(Services.SPACE_SERVICE.name(),"sockets space disconnect error", HttpStatus.NOT_FOUND);
            var user_space = UserSubListener.builder()
                    .username(person.getUserDetail().getUsername())
                    .avatar(person.getAvatar())
                    .uuid(uuid)
                    .build();
            // Remove the client from the room
            socketService.leftRoom(space.get(), client, user_space);
        };
    }
}
