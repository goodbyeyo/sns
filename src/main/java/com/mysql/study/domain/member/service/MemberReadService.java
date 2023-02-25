package com.mysql.study.domain.member.service;

import com.mysql.study.domain.member.dto.MemberDto;
import com.mysql.study.domain.member.entity.Member;
import com.mysql.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;

    public MemberDto getMember(Long id) {
        var member = memberRepository.findById(id).orElseThrow();
        return MemberDto.of(member);
    }
}
