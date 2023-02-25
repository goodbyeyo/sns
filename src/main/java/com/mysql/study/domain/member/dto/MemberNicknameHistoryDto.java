package com.mysql.study.domain.member.dto;

import com.mysql.study.domain.member.entity.MemberNameHistory;

import java.time.LocalDateTime;

public record MemberNicknameHistoryDto(
    Long id,
    Long memberId,
    String nickname,
    LocalDateTime createdAt
) {
    public static MemberNicknameHistoryDto of(MemberNameHistory history) {
        return new MemberNicknameHistoryDto(
                history.getId(),
                history.getMemberId(),
                history.getNickname(),
                history.getCreatedAt());
    }
}
