package com.mysql.study.domain.member.service;

import com.mysql.study.domain.member.dto.RegisterMemberCommand;
import com.mysql.study.domain.member.entity.Member;
import com.mysql.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;

     // 회원정보(이메일, 닉네임, 생년월일) 등록, 닉네임 10자리 제한
    public Member register(RegisterMemberCommand command) {
        var member = Member.builder()
                .nickName(command.nickname())
                .email(command.email())
                .birthDay(command.birthDay())
                .build();
        return memberRepository.save(member);
    }
}
