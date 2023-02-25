package com.mysql.study.domain.member.dto;

import com.mysql.study.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberDto (
        Long id,
        String email,
        String nickName,
        LocalDate birthDay
){
    public static MemberDto of(Member member) {
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getNickname(),
                member.getBirthday()
        );
    }
}
