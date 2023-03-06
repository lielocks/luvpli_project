package com.mainproject.server.chatroom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainproject.server.chatroom.dto.SubscribeDto;
import com.mainproject.server.chatroom.dto.SubscribeType;
import com.mainproject.server.chatroom.service.UserTimeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserTimeService userTimeService;

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String subscribeMessage = new String(message.getBody());
        SubscribeDto subscribe = objectMapper.readValue(subscribeMessage, SubscribeDto.class);
        log.info("[subscribe][message] {}", subscribeMessage);

        if (SubscribeType.BROAD_CAST == subscribe.getType()) {
            messagingTemplate.convertAndSend("/topic/message", subscribe);
            return;
        }
        userTimeService.updateTime(subscribe.getName(), subscribe.getTime());
        messagingTemplate.convertAndSendToUser(subscribe.getName(), "/topic/data", subscribe);
    }
}
