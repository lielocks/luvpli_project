package com.mainproject.server.playlist.mapper;

import com.mainproject.server.member.entity.Member;
import com.mainproject.server.playlist.dto.*;
import com.mainproject.server.playlist.entity.Playlist;
import com.mainproject.server.playlist.entity.PlaylistItem;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    List<PlaylistResponseDto> playlistToPlaylistResponseDtoList(List<Playlist> playlistList);

    List<SimplePlaylistResponseDto> playlistToSimplePlaylistResponseDtoList(List<Playlist> playlistList);

    SimplePlaylistResponseDto playlistToSimplePlaylistResponseDto(Playlist playlistList);

    default Playlist playlistPostDtoToPlaylist(PlaylistPostDto playlistPostDto, Member member) {
        if (playlistPostDto == null) {
            return null;
        } else {
            Playlist playlist = new Playlist();
            playlist.setTitle(playlistPostDto.getTitle());
            playlist.setMember(member);
            playlist.setCategoryList(playlistPostDto.getCategoryList());
            playlist.setStatus(playlistPostDto.isStatus());

            return playlist;
        }
    }

    default Playlist playlistPatchDtoToPlaylist(PlaylistPatchDto playlistPatchDto) {
        if (playlistPatchDto == null) {
            return null;
        } else {
            Playlist playlist = new Playlist();
            playlist.setPlaylistId(playlistPatchDto.getPlaylistId());
            playlist.setTitle(playlistPatchDto.getTitle());
            playlist.setCategoryList(playlistPatchDto.getCategoryList());
            playlist.setStatus(playlistPatchDto.isStatus());

            return playlist;
        }
    }

    default PlaylistResponseDto playlistToPlaylistResponseDto(Playlist playlist) {
        if (playlist == null) {
            return null;
        } else {
            PlaylistResponseDto.PlaylistResponseDtoBuilder playlistResponseDto = PlaylistResponseDto.builder();
            List<PlaylistItem> playlistItems = playlist.getPlaylistItems();
            playlistResponseDto.playlistId(playlist.getPlaylistId());
            playlistResponseDto.title(playlist.getTitle());
            playlistResponseDto.createdAt(playlist.getCreatedAt());
            playlistResponseDto.modifiedAt(playlist.getModifiedAt());
            playlistResponseDto.memberId(playlist.getMember().getMemberId());
            playlistResponseDto.name(playlist.getMember().getName());

            playlistResponseDto.like(playlist.getLikes().size());
            playlistResponseDto.categoryList(playlist.getCategoryList());
            playlistResponseDto.status(playlist.isStatus());

            playlistResponseDto.playlistItems(playlistItemsToPlaylistItemResponseDto(playlistItems));
            return playlistResponseDto.build();
        }
    }

    default List<PlaylistItemResponseDto> playlistItemsToPlaylistItemResponseDto(List<PlaylistItem> playlistItems) {
        return  playlistItems
                .stream()
                .map(playlistItem -> PlaylistItemResponseDto
                        .builder()
                        .url(playlistItem.getUrl())
                        .channelTitle(playlistItem.getChannelTitle())
                        .thumbnail(playlistItem.getThumbnail())
                        .videoId(playlistItem.getVideoId())
                        .title(playlistItem.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    default LikePlaylistResponseDto playlistToDetailPlaylistResponseDto(Playlist playlist, Boolean likeState, Boolean bookmarkState) {
        if (playlist == null) {
            return null;
        } else {
            LikePlaylistResponseDto.LikePlaylistResponseDtoBuilder likePlaylistResponseDto = LikePlaylistResponseDto.builder();
            List<PlaylistItem> playlistItems = playlist.getPlaylistItems();
            likePlaylistResponseDto.playlistId(playlist.getPlaylistId());
            likePlaylistResponseDto.title(playlist.getTitle());
            likePlaylistResponseDto.memberId(playlist.getMember().getMemberId());
            likePlaylistResponseDto.name(playlist.getMember().getName());
            likePlaylistResponseDto.like(playlist.getLikes().size());
            likePlaylistResponseDto.categoryList(playlist.getCategoryList());
            likePlaylistResponseDto.status(playlist.isStatus());
            likePlaylistResponseDto.likeState(likeState);
            likePlaylistResponseDto.bookmarkState(bookmarkState);
            likePlaylistResponseDto.playlistItems(playlistItemsToPlaylistItemResponseDto(playlistItems));
            return likePlaylistResponseDto.build();
        }
    }

    default LikePlaylistResponseDto playlistToLikePlaylistResponseDto(Playlist playlist, Boolean likeState, Boolean bookmarkState) {
        if (playlist == null) {
            return null;
        } else {
            LikePlaylistResponseDto.LikePlaylistResponseDtoBuilder likePlaylistResponseDto = LikePlaylistResponseDto.builder();
            List<PlaylistItem> playlistItems = playlist.getPlaylistItems();
            likePlaylistResponseDto.playlistId(playlist.getPlaylistId());
            likePlaylistResponseDto.title(playlist.getTitle());
            likePlaylistResponseDto.memberId(playlist.getMember().getMemberId());
            likePlaylistResponseDto.name(playlist.getMember().getName());
            likePlaylistResponseDto.categoryList(playlist.getCategoryList());
            likePlaylistResponseDto.status(playlist.isStatus());
            if (likeState == true){likePlaylistResponseDto.like(playlist.getLikes().size()+1);}
            if (likeState == false){likePlaylistResponseDto.like(playlist.getLikes().size()-1);}
            likePlaylistResponseDto.likeState(likeState);
            likePlaylistResponseDto.bookmarkState(bookmarkState);
            likePlaylistResponseDto.playlistItems(playlistItemsToPlaylistItemResponseDto(playlistItems));
            return likePlaylistResponseDto.build();
        }
    }
    default List<LikePlaylistResponseDto> playlistToPlaylistResponseDtoList(List<Playlist> playlistList, List<Boolean> bookmarkStates) {
        if ( playlistList == null ) {
            return null;
        }

        List<LikePlaylistResponseDto> list = new ArrayList<>( playlistList.size() );
        for ( int i = 0; i<playlistList.size(); i++ ) {
            list.add( playlistToDetailPlaylistResponseDto(playlistList.get(i), true ,bookmarkStates.get(i) ) );
        }

        return list;
    }
}
