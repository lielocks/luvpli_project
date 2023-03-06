package com.mainproject.server.chatroom.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ChatRoomPatchDto {
    private String roomId;
    private String title;
    private String pwd;
//    private Integer maxCount;
    private boolean secret;
}