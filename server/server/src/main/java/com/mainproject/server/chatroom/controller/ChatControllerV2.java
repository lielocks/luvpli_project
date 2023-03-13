package com.mainproject.server.chatroom.controller;

import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.service.ChatRedisService;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.member.service.MemberService;
import com.mainproject.server.playlist.entity.Playlist;
import com.mainproject.server.playlist.service.PlaylistService;
import com.mainproject.server.tx.NeedMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/roomsV2")
public class ChatControllerV2 {
    private final ChatRedisService chatRedisService;
    private final MemberService memberService;
    private final PlaylistService playlistService;

    @NeedMemberId
    @PostMapping
    public ChatRoom createRoom(@RequestBody String title, Long authMemberId, Playlist playlist) {
        Member member = memberService.findMember(authMemberId);
        Playlist playlist1 = playlistService.findPlaylist(playlist.getPlaylistId());
        return chatRedisService.createChatRoom(title, member,playlistService.findPlaylist(playlist1.getPlaylistId()));
    }

    @GetMapping
    public List<ChatRoom> findAllRooms() {
        List<ChatRoom> chatRooms = chatRedisService.findAllRoom();
        chatRooms.stream().forEach(room -> room.setUserCount(chatRedisService.getUserCount(room.getRoomId())));
        return chatRooms;
    }



}
