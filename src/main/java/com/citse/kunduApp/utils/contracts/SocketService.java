package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Space;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.models.POST;
import com.citse.kunduApp.utils.models.SocketType;
import com.citse.kunduApp.utils.models.UserSubListener;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketService {

    private final PersonDao personDao;
    private final Map<String, Set<Object>> room_listeners = new HashMap<>();

    public void sendMessage(String roomCode, SocketIOClient senderClient, String message, String username) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(roomCode).getClients()) {
            if(!client.getSessionId().equals(senderClient.getSessionId())){
                client.sendEvent("sms", POST.builder().type(SocketType.MESSAGE)
                        .message(message).username(username).build());
            }
        }
    }

    public void join(Space space, SocketIOClient client, UserSubListener user) {
        room_listeners.computeIfAbsent(space.getCode(), c -> new HashSet<>()).add(user);

        Set<Object> subs = room_listeners.get(space.getCode());
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("users_list",subs);
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("room_acts",
                POST.builder().type(SocketType.NOTIFICATION).message("@"+user.getUsername()+" joined the room.")
                        .build());
    }

    public void leftRoom(Space space, SocketIOClient client, UserSubListener user) {
        room_listeners.get(space.getCode()).remove(user);

        Set<Object> subs = room_listeners.get(space.getCode());
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("users_list",subs);
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("room_acts",
                POST.builder().type(SocketType.NOTIFICATION).message("@"+user.getUsername()+" left!")
                        .build());
    }

}
