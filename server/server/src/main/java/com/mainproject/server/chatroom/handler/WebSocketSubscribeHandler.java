package com.mainproject.server.chatroom.handler;

import com.mainproject.server.chatroom.dto.SubscribeDto;
import com.mainproject.server.chatroom.dto.SubscribeType;
import com.mainproject.server.chatroom.service.UserTimeService;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public class WebSocketSubscribeHandler<S> implements ApplicationListener<SessionSubscribeEvent> {

    private SimpMessagingTemplate messageTemplate;
    private UserTimeService userTimeService;
    private static final String TARGET = "/topic/data";

    public WebSocketSubscribeHandler() {
        super();
    }

    public WebSocketSubscribeHandler(SimpMessagingTemplate messageTemplate, UserTimeService userTimeService) {
        this.userTimeService = userTimeService;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        String source = String.valueOf(((List) ((Map) event.getMessage().getHeaders().get("nativeHeaders")).get("destination")).get(0));

        if (source.contains(TARGET)) {
            Principal user = event.getUser();
            if (user == null || user.getName() == null) {
                return;
            }
            messageTemplate.convertAndSendToUser(user.getName(), TARGET, SubscribeDto.builder()
                    .type(SubscribeType.USER)
                    .time(userTimeService.getTime(user.getName(), 20))
                    .build()
            );
        }
    }
}
