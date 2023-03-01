package com.mysql.study.application.facade;

import com.mysql.study.domain.follow.dto.FollowDto;
import com.mysql.study.domain.follow.service.FollowReadService;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.service.PostReadService;
import com.mysql.study.util.CursorRequest;
import com.mysql.study.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetTimelinePostFacade {

    private final FollowReadService followReadService;
    private final PostReadService postReadService;

    // member id -> follower 조회 -> posts 조회
    // Pull Model
    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        var memberIds = followReadService.getFollowings(memberId)
                .stream()
                .map(FollowDto::toMemberId)
                .collect(Collectors.toList());
        return postReadService.getPosts(memberIds, cursorRequest);
    }

    // Timeline 조회 -> posts 조회
    // Push Model
    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        var memberIds = followReadService.getFollowings(memberId)
                .stream()
                .map(FollowDto::toMemberId)
                .collect(Collectors.toList());
        return postReadService.getPosts(memberIds, cursorRequest);
    }
}
