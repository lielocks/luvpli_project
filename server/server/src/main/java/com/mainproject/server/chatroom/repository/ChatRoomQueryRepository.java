package com.mainproject.server.chatroom.repository;

import com.mainproject.server.chatroom.dto.QSearchChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchCondition;
import com.mainproject.server.chatroom.entity.QChatRoom;
import com.mainproject.server.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mainproject.server.chatroom.entity.QChatRoom.chatRoom;
import static com.mainproject.server.member.entity.QMember.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ChatRoomQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public ChatRoomQueryRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    public List<SearchChatRoomDto> search(SearchCondition condition) {

        return jpaQueryFactory
                .select(new QSearchChatRoomDto(
                        member.memberId.as("memberId"),
                        member.name.as("userName"),
                        member.ranking,
                        chatRoom.maxCount,
                        chatRoom.roomId,
                        chatRoom.title
                ))
                .from(member)
                .leftJoin(member.chatRooms, chatRoom)
                .where(
                        titleEq(condition.getTitle()),
                        userNameEq(condition.getUserName()),
                        countGoe(condition.getCountGoe()),
                        countLoe(condition.getCountLoe()))
                .fetch();
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? chatRoom.title.eq(title) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return hasText(userName) ? member.name.eq(userName) : null;
    }

    private BooleanExpression countGoe(Integer countGoe) {
        return countGoe != null ? chatRoom.maxCount.goe(countGoe) : null;
    }

    private BooleanExpression countLoe(Integer countLoe) {
        return countLoe != null ? chatRoom.maxCount.goe(countLoe) : null;
    }
}

