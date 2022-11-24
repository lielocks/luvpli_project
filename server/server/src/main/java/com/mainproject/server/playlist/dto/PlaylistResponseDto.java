package com.mainproject.server.playlist.dto;

import com.mainproject.server.playlist.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistResponseDto {
    private long playlistId;

    private long memberId;

    private List<PlaylistItemResponseDto> playlistItems;

    private String name;

    private String title;

    private List<Category> categoryList;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


//    private boolean status;

//    private List<PlaylistResponseDto> playlistList;

}
