package com.citse.kunduApp.modules;

import com.citse.kunduApp.entity.Space;
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
public class SocketSpaceService {

    private final Map<String, Set<Object>> room_listeners = new HashMap<>();

    public void onConnect(String group, SocketIOClient client, String username){
        client.getNamespace().getRoomOperations(group).sendEvent("channel","@"+username+" is online");
    }
    public void onDisconnect(String group, SocketIOClient client, String username){
        client.getNamespace().getRoomOperations(group).sendEvent("channel","@"+username+" is offline");
    }

    public void sendChannelMessage(String group, SocketIOClient senderClient, String message, String username) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(group).getClients()) {
            if(!client.getSessionId().equals(senderClient.getSessionId())){
                client.sendEvent("sms_channel", POST.builder().type(SocketType.MESSAGE)
                  .message(message).username(username).build());
            }
        }
    }
    public void sendSpaceMessage(String space, SocketIOClient senderClient, String message, String username) {
        for (SocketIOClient client : senderClient.getNamespace().getRoomOperations(space).getClients()) {
            if(!client.getSessionId().equals(senderClient.getSessionId())){
                client.sendEvent("sms_space", POST.builder().type(SocketType.MESSAGE)
                        .message(message).username(username).build());
            }
        }
    }

    public void joinSpace(Space space, SocketIOClient client, UserSubListener user) {
        room_listeners.computeIfAbsent(space.getCode(), c -> new HashSet<>()).add(user);//add user to local variable

        Set<Object> subs = room_listeners.get(space.getCode());//send the list of listeners by an event
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("space_listeners",subs);
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("space_door",
                POST.builder().type(SocketType.NOTIFICATION).message("@"+user.getUsername()+" joined the room.")
                        .build());
    }

    public void leftSpace(Space space, SocketIOClient client, UserSubListener user) {
        room_listeners.get(space.getCode()).remove(user);// remove user from local variable

        Set<Object> subs = room_listeners.get(space.getCode());
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("space_listeners",subs);
        client.getNamespace().getRoomOperations(space.getCode()).sendEvent("space_door",
                POST.builder().type(SocketType.NOTIFICATION).message("@"+user.getUsername()+" left!")
                        .build());
    }

}
