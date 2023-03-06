package com.mainproject.server.chatroom.config;

import com.mainproject.server.chatroom.handler.WebSocketSubscribeHandler;
import com.mainproject.server.chatroom.service.UserTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.websocket.Session;

@Configuration
public class WebSocketHandlerConfig<S extends Session> {

    @Bean
    public WebSocketSubscribeHandler<S> webSocketSubscribeHandler(UserTimeService userTimeService, SimpMessagingTemplate messageTemplate) {
        return new WebSocketSubscribeHandler<>(messageTemplate, userTimeService);
    }

    @Bean
    public WebSocketUnSubscribeHandler<S> webSocketUnSubscribeHandler(UserTimeService userTimeService) {
        return new WebSocketUnSubscribeHandler<>(userTimeService);
    }
}
