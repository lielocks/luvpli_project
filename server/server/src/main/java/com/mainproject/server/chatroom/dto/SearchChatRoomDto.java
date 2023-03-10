package com.mainproject.server.chatroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SearchChatRoomDto {
    private Long memberId;
    private String userName;
    private Integer ranking;
    private Integer maxCount;
    private String roomId;
    private String title;

    @QueryProjection
    public SearchChatRoomDto(Long memberId, String userName, int ranking, int maxCount, String roomId, String title) {
        this.memberId = memberId;
        this.userName = userName;
        this.ranking = ranking;
        this.maxCount = maxCount;
        this.roomId = roomId;
        this.title = title;
    }
}
