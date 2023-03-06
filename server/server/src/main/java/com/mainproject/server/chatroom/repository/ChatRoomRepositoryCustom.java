package com.mainproject.server.chatroom.repository;

import com.mainproject.server.chatroom.dto.ResponseChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ResponseChatRoomDto> search(SearchCondition searchCondition);

    List<ResponseChatRoomDto> search(SearchCondition searchCondition,
                                     Pageable pageable);
}
