package com.mysql.study.domain.follow.service;

import com.mysql.study.domain.follow.entity.Follow;
import com.mysql.study.domain.follow.repository.FollowRepository;
import com.mysql.study.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class FollowWriteService {

    private final FollowRepository followRepository;
    // from, to 회원정보 받아서 저장
    // from -> to validate
    // 서로 다른 도메인의 흐름을 제어 -> 헥사고날 아키텍처, DDD, Layered Architecture
    public void create(MemberDto fromMember, MemberDto toMember) {
        Assert.isTrue(!Objects.equals(fromMember.id(), toMember.id()),
                "from, to 회원은 다른 회원이어야 합니다.");

        var follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();
        followRepository.save(follow);
    }
}
