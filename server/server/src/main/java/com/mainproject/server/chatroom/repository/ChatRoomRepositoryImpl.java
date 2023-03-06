//package com.mainproject.server.chatroom.repository;
//
//import com.mainproject.server.chatroom.dto.QResponseChatRoomDto;
//import com.mainproject.server.chatroom.dto.ResponseChatRoomDto;
//import com.mainproject.server.chatroom.dto.SearchCondition;
//import com.mainproject.server.member.entity.QRankingList;
//import com.querydsl.core.QueryResults;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//import static com.mainproject.server.chatroom.entity.QChatRoom.chatRoom;
//import static com.mainproject.server.member.entity.QMember.member;
//import static com.mainproject.server.playlist.entity.QPlaylist.playlist;
//
//public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{
//
//    private final JPAQueryFactory queryFactory;
//
//    public ChatRoomRepositoryImpl(EntityManager em) {
//        queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Override
//    public List<ResponseChatRoomDto> search(SearchCondition searchCondition) {
//        return null;
//    }
//
//
//    this.roomId = chatRoom.getRoomId();
//        this.title = chatRoom.getTitle();
//        this.maxCount = chatRoom.getMaxCount();
//        this.pwd = chatRoom.getPwd();
//        this.secret = chatRoom.getPwd() != null;
//        this.userlist = chatRoom.getUserlist();
//        this.userSize = chatRoom.getUserSize();
//        this.playlistId = chatRoom.getPlaylistId();
//        this.memberResponseDto = memberResponseDto;
//        this.playlistResponseDto = playlistResponseDto;
//
//    @Override
//    public List<ResponseChatRoomDto> search(SearchCondition searchCondition,
//                                            Pageable pageable) {
//        queryFactory
//                .select(new QResponseChatRoomDto(
//                        chatRoom.roomId,
//                        chatRoom.title,
//                        chatRoom.maxCount,
//                        chatRoom.pwd,
//                        chatRoom.secret,
//                        chatRoom.userlist,
//                        chatRoom.userSize,
//                        playlist.playlistId
//        ))
//                .from(chatRoom)
//    }
//
//    @Override
//    public Page<MemberTeamDto> searchPageSimple(MemberSearchCondition condition,
//                                                Pageable pageable) {
//        QueryResults<MemberTeamDto> results = queryFactory
//                .select(new QMemberTeamDto(
//                        member.id,
//                        member.username,
//                        member.age,
//                        team.id,
//                        team.name))
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(usernameEq(condition.getUsername()),
//                        teamNameEq(condition.getTeamName()),
//                        ageGoe(condition.getAgeGoe()),
//                        ageLoe(condition.getAgeLoe()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();
//        List<MemberTeamDto> content = results.getResults();
//        long total = results.getTotal();
//        return new PageImpl<>(content, pageable, total);
//    }
//}
