package com.mainproject.server.chatroom.config;

import com.mainproject.server.chatroom.dto.SubscribeDto;
import com.mainproject.server.chatroom.dto.SubscribeType;
import com.mainproject.server.chatroom.service.UserTimeService;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public class WebSocketUnSubscribeHandler<S> implements ApplicationListener<SessionUnsubscribeEvent> {

    private UserTimeService userTimeService;
    private static final String TARGET = "/topic/data";

    public WebSocketUnSubscribeHandler() {
        super();
    }

    public WebSocketUnSubscribeHandler(UserTimeService userTimeService) {
        this.userTimeService = userTimeService;
    }

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        String source =
                String.valueOf(
                        ((List)
                                ((Map) event
                        .getMessage()
                        .getHeaders()
                        .get("nativeHeaders"))
                        .get("destination")
                        )
                .get(0));

        if (source.contains(TARGET)) {
            Principal user = event.getUser();
            if (user == null || user.getName() == null) {
                return;
            }
            userTimeService.deleteTime(user.getName());
        }
    }

}
