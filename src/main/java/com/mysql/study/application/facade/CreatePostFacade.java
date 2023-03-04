package com.mysql.study.application.facade;

import com.mysql.study.domain.follow.dto.FollowDto;
import com.mysql.study.domain.follow.service.FollowReadService;
import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.service.PostReadService;
import com.mysql.study.domain.post.service.PostWriteService;
import com.mysql.study.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CreatePostFacade {

    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand) {
        var postId = postWriteService.create(postCommand);
        var followerMemberIds = followReadService
                .getFollowers(postCommand.memberId())
                .stream()
                .map(FollowDto::fromMemberId)
                .toList();
        timelineWriteService.deliveryToTimeline(postId, followerMemberIds);
        return postId;
    }
}
