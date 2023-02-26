package com.mysql.study.application.facade;

import com.mysql.study.domain.follow.service.FollowWriteService;
import com.mysql.study.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Facade : 도메인 서비스의 흐름을 제어하는 서비스
 */
@RequiredArgsConstructor
@Service
public class CreateFollowMemberFacade {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;

    public void execute(Long fromMemberId, Long toMemberId) {
        // fromMemberId, toMemberId 회원정보를 조회 후 팔로우를 생성
        var fromMember = memberReadService.getMember(fromMemberId);
        var toMember = memberReadService.getMember(toMemberId);

        followWriteService.create(fromMember, toMember);
    }
}
