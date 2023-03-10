package com.mainproject.server.chatroom.repository;

import com.mainproject.server.chatroom.dto.ResponseChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchCondition;
import com.mainproject.server.chatroom.dto.SortChatRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<SearchChatRoomDto> search(SearchCondition searchCondition);

    Page<SearchChatRoomDto> searchPageSimple(SearchCondition searchCondition, Pageable pageable);
    Page<SearchChatRoomDto> searchPageComplex(SearchCondition searchCondition, Pageable pageable);
    Page<SearchChatRoomDto> sortChatRoom(SearchCondition searchCondition, Pageable pageable);
}
