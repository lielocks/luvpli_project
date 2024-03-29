package com.mainproject.server.chatroom.repository;

import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>, ChatRoomRepositoryCustom, QuerydslPredicateExecutor<ChatRoom> {
    Optional<ChatRoom> findByRoomId(String roomId);
    Page<ChatRoom> findByTitleContaining(String keyword, Pageable pageable);
    List<ChatRoom> findByTitleContaining(String title);
    List<ChatRoom> findByMember(Member member);
    List<ChatRoom> findByPlaylistId(Long playlistId);

    @Query(value = "select * from ChatRoom c where c.title = 'title1'",
            nativeQuery = true)
    ChatRoom findChatRoomByTitle(String title);

}
