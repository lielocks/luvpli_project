package com.mainproject.server.chatroom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainproject.server.chatroom.config.WebSocketTest;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.repository.ChatRoomRepository;
import com.mainproject.server.exception.BusinessException;
import com.mainproject.server.exception.ExceptionCode;
import com.mainproject.server.member.mapper.MemberMapper;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.member.repository.MemberRepository;
import com.mainproject.server.playlist.entity.Playlist;
import com.mainproject.server.playlist.repository.PlaylistRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberMapper memberMapper;
    private final WebSocketTest webSocketTest;
    private final MemberRepository memberRepository;
    private final PlaylistRepository playlistRepository;

    public ChatRoom createRoom(ChatRoom chatRoom) {

//        String randomId = UUID.randomUUID().toString();
//        chatRoom.setRoomId(randomId);
        if (chatRoom.getPwd() != null) chatRoom.setSecret(true);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return savedChatRoom;
    }

    public ChatRoom updateRoom(ChatRoom chatRoom) {
        ChatRoom verifiedRoom = findVerifiedRoomId(chatRoom.getRoomId());

        Optional.ofNullable(chatRoom.getTitle())
                .ifPresent(title -> verifiedRoom.setTitle(title));
        Optional.ofNullable(chatRoom.getPwd())
                .ifPresent(pwd -> verifiedRoom.setPwd(pwd));

        if (verifiedRoom.getPwd() != null) verifiedRoom.setSecret(true);

        return chatRoomRepository.save(verifiedRoom);
    }

    public Page<ChatRoom> findChatRooms(int page, int size) {
        Page<ChatRoom> findAllRooms = chatRoomRepository.findAll(
                PageRequest.of(page, size, Sort.by("roomId").descending()));

        return findAllRooms;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        //webSocketTest.onMessage("");
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public ChatRoom findVerifiedRoomId(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ROOM_NOT_EXISTS));
        return chatRoom;
    }

    public Page<ChatRoom> searchChatRooms(int page, int size, String tab, String q) {
        if (tab.equals("popular")) tab = "chatroom.memberCount";
        Page<ChatRoom> chatRooms = chatRoomRepository.findByTitleContaining(q, PageRequest.of(page, size));
        return chatRooms;
    }

    public void deleteChatRoom(String roomId) {
        ChatRoom room = findVerifiedRoomId(roomId);
        chatRoomRepository.delete(room);
    }

    public Page<ChatRoom> findRoomsUserSize(int page, int size) {
        Page<ChatRoom> findAllRooms = chatRoomRepository.findAll(
                PageRequest.of(page, size, Sort.by("userSize").descending()));

        return findAllRooms;
    }

    public Page<ChatRoom> searchChatRooms(String type, String name, int page, int size) {

        List<ChatRoom> searchChatRooms = new ArrayList<>();

        if (type.equals("title")) {
            //?????? ???????????? ???????????? ?????????????????? ??????
            List<ChatRoom> chatRooms = chatRoomRepository.findByTitleContaining(name);
            for (ChatRoom chatRoom : chatRooms) {
                // ?????? ????????? ???????????? ??????
//                    if (chatRoom.getOnair().equals(ChatRoom.Onair.ON))
                searchChatRooms.add(chatRoom);
            }
        }
        else if (type.equals("name")) {
            // ?????? ????????? ????????? member ??????
            List<Member> searchMembers = memberRepository.findByNameContaining(name);
            for (Member member : searchMembers) {
                // ?????? member?????? ????????? ??? ??????
                List<ChatRoom> chatRooms = chatRoomRepository.findByMember(member);
                for (ChatRoom chatRoom : chatRooms) {
                    // ?????? ????????? ???????????? ??????
//                    if (chatRoom.getOnair().equals(ChatRoom.Onair.ON))
                    searchChatRooms.add(chatRoom);
                }
            }
        }
        else if (type.equals("category")) {
            // ?????? ??????????????? ???????????? ?????????????????? ??????
//            List<String> search = new ArrayList<>();
//            search.add(name);
//            searchPlaylists = playlistRepository.findByCategoryListContaining(search);

            // ?????? ????????????????????? ???????????? ??????????????? ???????????? ???????????? ??????
            // ????????????????????? ?????? ???????????? ????????? ????????????????????? ???????????? ????????? ??????
            List<Playlist> allPlaylists = playlistRepository.findAll();
            List<ChatRoom> chatRooms = new ArrayList<>();
            ChatRoom findChatRoom = new ChatRoom();
            for (Playlist playlist : allPlaylists){
                for (int i=0; i<playlist.getCategoryList().size(); i++){
                    for (int j=0; j<chatRoomRepository.findByPlaylistId(playlist.getPlaylistId()).size(); j++) {
                        findChatRoom = chatRoomRepository.findByPlaylistId(playlist.getPlaylistId()).get(0);
                    }
                    if (playlist.getCategoryList().get(i).equals(name)
//                    && findChatRoom.getOnair().equals(ChatRoom.Onair.ON)
                    ){
                        chatRooms.add(findChatRoom);
                    }
                }
            }
            // ?????? ??????
            searchChatRooms = chatRooms.stream().distinct().collect(Collectors.toList());
        }
        else {throw new BusinessException(ExceptionCode.BAD_REQUEST);
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("userCount").descending());
        int start = (int)pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), searchChatRooms.size());

        Page<ChatRoom> chatRoomPage = new PageImpl<>(searchChatRooms.subList(start, end), pageRequest, searchChatRooms.size());
        return chatRoomPage;
    }
}
