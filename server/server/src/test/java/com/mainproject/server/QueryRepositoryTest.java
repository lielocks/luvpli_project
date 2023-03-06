package com.mainproject.server;

import com.mainproject.config.SecurityTestConfig;
import com.mainproject.server.chatroom.dto.SearchCondition;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.repository.ChatRoomRepository;
import com.mainproject.server.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(SecurityTestConfig.class)
@Transactional
public class QueryRepositoryTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    JPAQueryFactory jpaQueryFactory;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void before() {

        jpaQueryFactory = new JPAQueryFactory(em);

        ChatRoom chatRoomA = new ChatRoom("title1", 10, "pwd1");
        ChatRoom chatRoomB = new ChatRoom("title2", 20, "pwd2");
        ChatRoom chatRoomC = new ChatRoom("title3", 30, "pwd3");
        ChatRoom chatRoomD = new ChatRoom("title4", 40, "pwd4");

        Member member1 = new Member(1L, "one");
        Member member2 = new Member(2L, "two");

        em.persist(chatRoomA);
        em.persist(chatRoomB);
        em.persist(chatRoomC);
        em.persist(chatRoomD);

        em.persist(member1);
        em.persist(member2);
    }


    @Test
    public void search(SearchCondition condition) {
        ChatRoom chatRoom = ChatRoom.builder().title(condition.getTitle()).build();
        chatRoomRepository.search(condition);
    }


}
