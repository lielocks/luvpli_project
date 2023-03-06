package com.mainproject.server.chatroom.controller;

import com.mainproject.server.chatroom.dto.ChatRoomPostDto;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.mapper.ChatRoomMapper;
import com.mainproject.server.chatroom.service.ChatRedisService;
import com.mainproject.server.chatroom.service.ChatService;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.member.repository.MemberRepository;
import com.mainproject.server.member.service.MemberService;
import com.mainproject.server.playlist.entity.Playlist;
import com.mainproject.server.playlist.service.PlaylistService;
import com.mainproject.server.response.MultiResponseDto;
import com.mainproject.server.response.SingleResponseDto;
import com.mainproject.server.tx.NeedMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/roomsV2")
public class RoomControllerV2 {
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
