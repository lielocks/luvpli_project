package com.mainproject.server.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SortChatRoomDto {

    private Integer maxCount;
    private Integer ranking;

    @QueryProjection
    public SortChatRoomDto(Integer maxCount, Integer ranking) {
        this.maxCount = maxCount;
        this.ranking = ranking;
    }
}
