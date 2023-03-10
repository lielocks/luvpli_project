package com.mainproject.server.chatroom.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class SearchCondition {

    private String title;
    private String userName;
    private Integer countGoe;
    private Integer countLoe;
    private Integer ranking;

}
