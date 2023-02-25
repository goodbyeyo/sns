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
    private String nickname;
    private final String email;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;
    private static final Long NAME_MAX_LENGTH = 10L;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthday, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.email = Objects.requireNonNull(email);
        this.birthday = Objects.requireNonNull(birthday);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public void changeNickName(String changeName) {
        Objects.requireNonNull(changeName);
        validateNickName(changeName);
        this.nickname = changeName;
    }

    private void validateNickName(String nickName) {
        Assert.isTrue(nickName.length() <= NAME_MAX_LENGTH, "nickName must be less than 10 characters");
    }
}
