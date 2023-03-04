package com.mysql.study.domain.follow.dto;

import com.mysql.study.domain.follow.entity.Follow;

public record FollowDto (
    Long toMemberId,
    Long fromMemberId
) {
    public static FollowDto of(Follow follow) {
        return new FollowDto(follow.getToMemberId(), follow.getFromMemberId());
    }
}
