package com.mysql.study.domain.member.service;

import com.mysql.study.domain.member.dto.RegisterMemberCommand;
import com.mysql.study.domain.member.entity.Member;
import com.mysql.study.domain.member.entity.MemberNameHistory;
import com.mysql.study.domain.member.repository.MemberNickNameHistoryRepository;
import com.mysql.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberNickNameHistoryRepository memberNickNameHistoryRepository;

     // 회원정보(이메일, 닉네임, 생년월일) 등록, 닉네임 10자리 제한
    public Member register(RegisterMemberCommand command) {
        var member = Member.builder()
                .nickname(command.nickname())
                .email(command.email())
                .birthday(command.birthDay())
                .build();
        var savedMember = memberRepository.save(member);

        var history = MemberNameHistory.saveMemberNickNameHistory(savedMember);
        memberNickNameHistoryRepository.save(history);
        return savedMember;
    }

    public void changeNickName(Long memberId, String nickName) {
        var member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickName(nickName);
        memberRepository.save(member);

        var history = MemberNameHistory.saveMemberNickNameHistory(member);
        memberNickNameHistoryRepository.save(history);
    }

}
