package com.mysql.study.domain.member.service;

import com.mysql.study.domain.member.dto.MemberDto;
import com.mysql.study.domain.member.dto.MemberNicknameHistoryDto;
import com.mysql.study.domain.member.entity.Member;
import com.mysql.study.domain.member.entity.MemberNameHistory;
import com.mysql.study.domain.member.repository.MemberNickNameHistoryRepository;
import com.mysql.study.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final MemberNickNameHistoryRepository memberNickNameHistoryRepository;

    public MemberDto getMember(Long id) {
        var member = memberRepository.findById(id).orElseThrow();
        return MemberDto.of(member);
    }

    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
        return memberNickNameHistoryRepository.findAllByMemberId(memberId)
                .stream()
                .map(MemberNicknameHistoryDto::of)
                .collect(Collectors.toList());
    }

    public List<MemberDto> getMembers(List<Long> ids) {
        var members = memberRepository.findAllByIdIn(ids);
        return members.stream()
                .map(MemberDto::of)
                .collect(Collectors.toList());
    }
}
