package com.mainproject.server;

import com.mainproject.config.SecurityTestConfig;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.repository.ChatRoomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.mainproject.server.chatroom.entity.QChatRoom.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(SecurityTestConfig.class)
@Transactional
public class QueryBasicTest {

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

        em.persist(chatRoomA);
        em.persist(chatRoomB);
        em.persist(chatRoomC);
        em.persist(chatRoomD);

    }

    @Test
    public void searchChatRoom() {
        ChatRoom findChatRoom = jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.title.eq("title3")
                        .and(chatRoom.maxCount.between(20, 40)))
                .fetchOne();

        assertThat(findChatRoom.getTitle()).isEqualTo("title3");
    }

    @Test
    public void searchParam() {
        ChatRoom findChatRoom = jpaQueryFactory
                .selectFrom(chatRoom)
                .where(
                        chatRoom.title.eq("title3"),
                        chatRoom.maxCount.eq(30)
                )
                .fetchOne();

        assertThat(findChatRoom.getMaxCount()).isEqualTo(30);
    }

    @Test
    public void searchTitle() {
        int max = chatRoomRepository.findChatRoomByTitle("title1").getMaxCount();
        ChatRoom room = jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.title.eq("title1"),
                        chatRoom.maxCount.eq(max))
                .fetchOne();

        assertThat(room.getTitle()).isEqualTo("title1");
    }

}
