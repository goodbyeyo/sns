package com.mysql.study.domain.member.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 과거의 이력을 저장하는 필드는 정규화의 대상이 아니다.
 */
@Getter
public class MemberNameHistory {

    private final Long id;
    private final Long memberId;
    private final String nickname;
    private final LocalDateTime createdAt;

    @Builder
    public MemberNameHistory(Long id, Long memberId, String nickname, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.nickname = Objects.requireNonNull(nickname);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public static MemberNameHistory saveMemberNickNameHistory(Member member) {
        return MemberNameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
    }
}
