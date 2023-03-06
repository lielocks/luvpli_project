package com.mainproject.server.chatroom.dto;

import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.member.dto.SimpleMemberResponseDto;
import com.mainproject.server.playlist.dto.PlaylistResponseDto;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ResponseChatRoomDto {
    private String roomId;
    private String title;
    private int maxCount;

    public ResponseChatRoomDto(String roomId, String title, int maxCount, String pwd, boolean secret, int userSize, Long playlistId, SimpleMemberResponseDto memberResponseDto, PlaylistResponseDto playlistResponseDto, List<String> userlist) {
        this.roomId = roomId;
        this.title = title;
        this.maxCount = maxCount;
        this.pwd = pwd;
        this.secret = secret;
        this.userSize = userSize;
        this.playlistId = playlistId;
        this.memberResponseDto = memberResponseDto;
        this.playlistResponseDto = playlistResponseDto;
        this.userlist = userlist;
    }

    private String pwd;
    private boolean secret;
    private int userSize;
    private Long playlistId;
    SimpleMemberResponseDto memberResponseDto;
    PlaylistResponseDto playlistResponseDto;
    List<String> userlist = new ArrayList<>();

    @Builder
    @QueryProjection
    public ResponseChatRoomDto(ChatRoom chatRoom, SimpleMemberResponseDto memberResponseDto, PlaylistResponseDto playlistResponseDto) {
        this.roomId = chatRoom.getRoomId();
        this.title = chatRoom.getTitle();
        this.maxCount = chatRoom.getMaxCount();
        this.pwd = chatRoom.getPwd();
        this.secret = chatRoom.getPwd() != null;
        this.userlist = chatRoom.getUserlist();
        this.userSize = chatRoom.getUserSize();
        this.playlistId = chatRoom.getPlaylistId();
        this.memberResponseDto = memberResponseDto;
        this.playlistResponseDto = playlistResponseDto;
    }
}
