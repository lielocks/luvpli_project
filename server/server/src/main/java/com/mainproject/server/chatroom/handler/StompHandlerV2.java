package com.mainproject.server.chatroom.handler;

import com.mainproject.server.chatroom.entity.ChatMessage;
import com.mainproject.server.chatroom.service.ChatRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandlerV2 implements ChannelInterceptor {

    private final ChatRedisService chatRedisService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == headerAccessor.getCommand()) {
            String memberName = headerAccessor.getFirstNativeHeader("memberName");
            log.info("CONNECT {},", memberName);
        } else if (StompCommand.MESSAGE == headerAccessor.getCommand()) {
            String roomId = chatRedisService.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")).orElse("InvalidRoomId"));
            String sessionId = String.valueOf(message.getHeaders().get("simpSessionId"));
            chatRedisService.setUserEnterInfo(sessionId, roomId);
            chatRedisService.plusUserCount(roomId);

            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            chatRedisService.sendMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).memberName(name).build());
            log.info("ENTER {} {}", roomId, name);
        } else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) {
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRedisService.getUserEnterRoomId(sessionId);

            chatRedisService.minusUserCount(roomId);

            String name = Optional.ofNullable((Principal) message.getHeaders()
                    .get("simpUser")).map(Principal::getName).orElse("UnknownUser");

            chatRedisService.sendMessage(ChatMessage.builder().type(ChatMessage.MessageType.LEAVE).roomId(roomId).memberName(name).build());

            chatRedisService.minusUserCount(roomId);
            chatRedisService.removeUserEnterInfo(sessionId);
            log.info("LEAVE {} {}", roomId, name);
        }

        return message;
    }

}
