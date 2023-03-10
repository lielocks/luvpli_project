package com.mainproject.server;

import com.mainproject.config.SecurityTestConfig;
import com.mainproject.server.chatroom.dto.SearchChatRoomDto;
import com.mainproject.server.chatroom.dto.SearchCondition;
import com.mainproject.server.chatroom.entity.ChatRoom;
import com.mainproject.server.chatroom.repository.ChatRoomRepository;
import com.mainproject.server.member.entity.Member;
import com.mainproject.server.member.entity.QMember;
import com.mainproject.server.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.core.JreMemoryLeakPreventionListener;
import org.assertj.core.api.AssertProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static com.mainproject.server.chatroom.entity.QChatRoom.*;
import static com.mainproject.server.member.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Import(SecurityTestConfig.class)
@Transactional
public class QueryBasicTest {

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    MemberRepository memberRepository;

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

        Member mem1 = new Member("one", 133);
        Member mem2 = new Member("two", 564);
        Member mem3 = new Member("three", 5230);

        em.persist(chatRoomA);
        em.persist(chatRoomB);
        em.persist(chatRoomC);
        em.persist(chatRoomD);

        em.persist(mem1);
        em.persist(mem2);
        em.persist(mem3);

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

    @Test
    public void sort() {
        jpaQueryFactory = new JPAQueryFactory(em);

        ChatRoom chatRoomA = new ChatRoom("title1", 10, "pwd1");
        ChatRoom chatRoomB = new ChatRoom("title2", 20, "pwd2");
        ChatRoom chatRoomC = new ChatRoom("title3", 30, "pwd3");
        ChatRoom chatRoomD = new ChatRoom("title4", 40, "pwd4");

        em.persist(chatRoomA);
        em.persist(chatRoomB);
        em.persist(chatRoomC);
        em.persist(chatRoomD);

        Member mem1 = new Member("one", 133);
        Member mem2 = new Member("two", 564);
        Member mem3 = new Member("three", 5230);

        em.persist(mem1);
        em.persist(mem2);
        em.persist(mem3);

        SearchCondition condition = new SearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<SearchChatRoomDto> result = chatRoomRepository.sortChatRoom(condition, pageRequest);
        assertThat(result.getSize()).isEqualTo(3);

        Iterable<Member> results = memberRepository.findAll((Pageable) member.ranking.between(1, 50000));
        for (Member member : results) {
            log.info("member =" + member );
        }
    }


}
