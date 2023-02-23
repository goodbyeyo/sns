package com.mysql.study.domain.member.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    private final Long id;
    private String nickname;
    private final String email;
    private final LocalDate birthDate;
    private final LocalDateTime createAt;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthDate, LocalDateTime createAt) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.email = Objects.requireNonNull(email);
        this.birthDate = Objects.requireNonNull(birthDate);
        this.createAt = createAt;
    }
}
