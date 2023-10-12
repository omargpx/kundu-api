package com.citse.kunduApp.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
public class SocketConfig {
    @Bean
    public SocketIOServer socketIOServer() throws UnknownHostException {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(8081);
        return new SocketIOServer(config);
    }
}
