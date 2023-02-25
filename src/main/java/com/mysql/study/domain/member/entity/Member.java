package com.mysql.study.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    private final Long id;
    private String nickName;
    private final String email;
    private final LocalDate birthDay;
    private final LocalDateTime createdAt;
    private static final Long NAME_MAX_LENGTH = 10L;

    @Builder
    public Member(Long id, String nickName, String email, LocalDate birthDay, LocalDateTime createdAt) {
        this.id = id;
        this.nickName = Objects.requireNonNull(nickName);
        this.email = Objects.requireNonNull(email);
        this.birthDay = Objects.requireNonNull(birthDay);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    void validateNickName(String nickName) {
        Assert.isTrue(nickName.length() <= NAME_MAX_LENGTH, "nickName must be less than 10 characters");

    }
}
