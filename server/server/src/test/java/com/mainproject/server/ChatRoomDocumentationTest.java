package com.mainproject.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainproject.config.SecurityTestConfig;
import com.mainproject.server.chatroom.controller.RoomController;
import com.mainproject.server.chatroom.dto.ResponseChatRoomDto;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.mapper.ChatRoomMapper;
import com.mainproject.server.chatroom.repository.ChatRoomRepository;
import com.mainproject.server.chatroom.service.ChatService;
import com.mainproject.server.member.dto.SimpleMemberResponseDto;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.playlist.dto.PlaylistResponseDto;
import com.mainproject.server.playlist.entity.Playlist;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(MockitoExtension.class)
public class ChatRoomDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ChatRoomMapper chatRoomMapper;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatService chatService;

    @Test
    @DisplayName("채팅방 업데이트 치고 그대로 저장되면 true 리턴")
    public void update() throws Exception {
//            //given
//        UUID chatRoomId = UUID.fromString("title1");
//
//        ChatRoom response = ChatRoom.builder()
//                .roomId(String.valueOf(chatRoomId))
//                .title("title1")
//                .pwd("gg")
//                .maxCount(4)
//                .build();
////        given(chatService.updateRoom(eq(new ChatRoom("title1", 4, "gg"))))
////                        .willReturn(response);
//        given(chatRoomRepository.findByRoomId(anyString())).willReturn(Optional.ofNullable((response)));
//
//
//        //when
//        ChatRoomPatchDto patch = new ChatRoomPatchDto();
//        patch.setRoomId(String.valueOf(chatRoomId));
//        patch.setTitle("title1");
//        patch.setPwd("gg");
//
//        ResultActions result = this.mockMvc.perform(
//                put("/rooms", patch)
//                        .content(objectMapper.writeValueAsString(patch))
//                        .contentType(MediaType.APPLICATION_JSON)
//        );
//
//            //then
//        result.andExpect(status().isOk())
//                .andDo(document("chatRoom-update",
//                        getDocumentRequest(),
//                        getDocumentResponse(),
//                        pathParameters(
//                                parameterWithName("title").description("채팅방이름")
//                        ),
//                        requestFields(
//                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메시지"),
//                                fieldWithPath("data.chatRoom.title").type(JsonFieldType.STRING).description("아이디"),
//                                fieldWithPath("data.chatRoom.maxCount").type(JsonFieldType.NUMBER).description("이름"),
//                                fieldWithPath("data.chatRoom.pwd").type(JsonFieldType.STRING).description("성")
//                        )
//                ));
        //given

        Member member = Member.builder().memberId(1L).name("min").build();
        PlaylistResponseDto playlistResponseDto = PlaylistResponseDto.builder().title("playlist1").build();
        UUID id = UUID.randomUUID();
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .playlistId(playlistResponseDto.getPlaylistId())
                .title(String.valueOf(id))
                .maxCount(4)
                .pwd("pwd1")
                .build();

        ChatRoom room = chatService.createRoom(chatRoom);

//        given(chatRoomRepository.findChatRoomByTitle(any())).willReturn(room.getTitle());

        //when
        ChatRoom updateRoom = ChatRoom.builder()
                .roomId(String.valueOf(id))
                .maxCount(4)
                .title("title1")
                .pwd("pwd1")
                .build();

        ChatRoom result = chatService.updateRoom(updateRoom);

        //then
        assertThat(result.getPwd()).isEqualTo("pwd1");
        assertThat(result.getMaxCount()).isEqualTo(4);

    }
}
