package com.mainproject.server.chatroom.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChatRoomPostDto {
    private Long memberId;
    private long playlistId;
    private String title;
    private int maxCount;
    private String pwd;
    private boolean secret;
}
