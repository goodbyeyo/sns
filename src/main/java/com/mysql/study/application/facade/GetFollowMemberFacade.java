package com.mysql.study.application.facade;

import com.mysql.study.domain.follow.dto.FollowDto;
import com.mysql.study.domain.follow.service.FollowReadService;
import com.mysql.study.domain.member.dto.MemberDto;
import com.mysql.study.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetFollowMemberFacade {

    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;

    public List<MemberDto> execute(Long memberId) {
        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream()
                .map(FollowDto::toMemberId)
                .collect(Collectors.toList());
        return memberReadService.getMembers(followingMemberIds);

        // 1. fromMemberId = memberId -> follow list
        // 2. 1번 결과를 순회하면서 회원정보 조회

    }

}
