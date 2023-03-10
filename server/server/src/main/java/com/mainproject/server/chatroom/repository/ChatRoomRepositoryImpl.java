package com.mainproject.server.chatroom.repository;

import com.mainproject.server.chatroom.dto.QSearchChatRoomDto;
import com.mainproject.server.chatroom.dto.ResponseChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchCondition;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.member.entity.QRankingList;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mainproject.server.chatroom.entity.QChatRoom.chatRoom;
import static com.mainproject.server.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<SearchChatRoomDto> search(SearchCondition condition) {
        return queryFactory
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

    @Override
    public Page<SearchChatRoomDto> searchPageSimple(SearchCondition condition, Pageable pageable) {
        QueryResults<SearchChatRoomDto> results = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SearchChatRoomDto> content = results.getResults();
        long count = results.getTotal();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<SearchChatRoomDto> searchPageComplex(SearchCondition condition, Pageable pageable) {
        List<SearchChatRoomDto> content = queryFactory
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
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Member> count = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.chatRooms, chatRoom)
                .where(
                        titleEq(condition.getTitle()),
                        userNameEq(condition.getUserName()),
                        countGoe(condition.getCountGoe()),
                        countLoe(condition.getCountLoe()));

        return PageableExecutionUtils.getPage(content, pageable, count::fetchCount);
    }

    @Override
    public Page<SearchChatRoomDto> sortChatRoom(SearchCondition condition, Pageable pageable) {

        QueryResults<SearchChatRoomDto> results = queryFactory
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
                        countLoe(condition.getCountLoe()),
                        rankingEq(condition.getRanking()))
                .orderBy(member.ranking.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SearchChatRoomDto> content = results.getResults();
        long count = results.getTotal();

        return new PageImpl<>(content, pageable, count);
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? chatRoom.title.eq(title) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return hasText(userName) ? member.name.eq(userName) : null;
    }

    private BooleanExpression rankingEq(Integer ranking) {
        return hasText(String.valueOf(ranking)) ? member.ranking.eq(ranking) : member.isNull();
    }
    private BooleanExpression countGoe(Integer countGoe) {
        return countGoe != null ? chatRoom.maxCount.goe(countGoe) : null;
    }

    private BooleanExpression countLoe(Integer countLoe) {
        return countLoe != null ? chatRoom.maxCount.goe(countLoe) : null;
    }

}
