package com.mysql.study.controller;

import com.mysql.study.domain.member.dto.MemberDto;
import com.mysql.study.domain.member.dto.MemberNicknameHistoryDto;
import com.mysql.study.domain.member.dto.RegisterMemberCommand;
import com.mysql.study.domain.member.service.MemberReadService;
import com.mysql.study.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Presentation Layer 에 Domain Entity 를 노출시키는것은 좋지 않다.
 * Presentation Layer 에서는 Domain Entity 를 사용하지 않고, DTO 를 사용한다.
 * entity 를 반환하면 JPA 의 경우 Open Session In View (OSIV) 이슈 발생할 수 있음
 * OSIV 는 트랜잭션을 커밋하지 않은 상태에서 HTTP 응답을 보내는 것을 말한다.
 * JPA 에서는 OEIV(Open EntityManager In View) 라고 부르지만 관례상 OSIV 라고 부른다.
 * 영속성 컨텍스트가 살아있으면 엔티티는 영속 상태로 관리되기때문에 지연 로딩을 사용할 수 있다.
 * 하지만 OSIV 는 트랜잭션을 커밋하지 않은 상태에서 HTTP 응답을 보내는 것이기 때문에
 * 트랜잭션을 커밋하지 않은 상태에서 지연 로딩을 사용하면 예상치 못한 문제가 발생할 수 있다.
 * OSIV 를 사용하면 트랜잭션을 커밋하지 않은 상태에서 지연 로딩을 사용하면
 * 지연 로딩으로 인해 데이터베이스에 쿼리가 발생하고, 이때 데이터베이스 커넥션을 가져오는데
 * 이미 트랜잭션을 커밋하지 않은 상태이기 때문에 데이터베이스 커넥션을 가져올 수 없다.
 * 이런 문제를 해결하기 위해 OSIV 를 사용하지 않는다.
 * OSIV 를 사용하지 않으면 트랜잭션을 커밋한 후에 지연 로딩을 사용할 수 있기 때문에
 * 지연 로딩으로 인해 데이터베이스에 쿼리가 발생하더라도 데이터베이스 커넥션을 가져올 수 있다.
 * OSIV 를 사용하면 같은 영속성 컨텍스트를 여러 트랜잭션이 공유 할 수 있기때문에 트랜잭션 롤백 시 주의해야 한다
 * 프리젠테이션 계층에서 엔티티를 수정하고 나서 비지니스 로직을 수행하면 엔티티가 수정 될 수 있다
 * 프리젠테이션 계층에서 지연 로딩에 의한 SQL 이 실행되면 성능 튜닝시에 확인해야 할 부분이 넓어진다
 * 그리고 너무 오랜시간 동안 데이터베이스 커넥션 리소스를 사용하게 된다
 * 따라서 실시간 트래픽이 중요한 서비스에서는 커넥션이 모자랄 수 있고 이것은 결국 장애로 이어질 수 있다
 * 그래서 실시간 트레픽이 중요한 경우 프리젠테이션 계층에서 엔티티를 직접 반환하지 않고 DTO 를 반환하도록 한다
 * 어드민 페이지 같이 실시간 트레픽이 중요하지 않은 경우 OSIV 를 사용해도 괜찮다
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberWriteService memberWriteService;
    private final MemberReadService memberReadService;

    @PostMapping
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member = memberWriteService.register(command);
        return MemberDto.of(member);
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PostMapping("/{id}/name")
    public MemberDto changeNickName(@PathVariable Long id, @RequestBody String nickName) {
        memberWriteService.changeNickName(id, nickName);
        return memberReadService.getMember(id);
    }

    @GetMapping("/{memberId}/nickname-histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId) {
        return memberReadService.getNicknameHistories(memberId);
    }
}
