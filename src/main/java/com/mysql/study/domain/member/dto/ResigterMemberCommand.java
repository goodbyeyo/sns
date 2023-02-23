package com.mysql.study.domain.member.dto;

import java.time.LocalDate;

public record ResigterMemberCommand (
    String email,
    String nickname,
    LocalDate birthDate
){
//    public ResigterMemberCommand {
//        if (email == null || email.isBlank()) {
//            throw new IllegalArgumentException("email must not be empty");
//        }
//        if (nickname == null || nickname.isBlank()) {
//            throw new IllegalArgumentException("nickname must not be empty");
//        }
//        if (birthDate == null) {
//            throw new IllegalArgumentException("birthDate must not be empty");
//        }
//    }
}
