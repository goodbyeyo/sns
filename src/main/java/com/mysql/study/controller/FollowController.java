package com.mysql.study.controller;

import com.mysql.study.application.facade.CreateFollowMemberFacade;
import com.mysql.study.application.facade.GetFollowMemberFacade;
import com.mysql.study.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {

    private final CreateFollowMemberFacade createFollowMemberFacade;
    private final GetFollowMemberFacade getFollowMemberFacade;

    @PostMapping("{fromId}/{toId}")
    public void registerFollow(@PathVariable Long fromId, @PathVariable Long toId) {
        createFollowMemberFacade.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollower(@PathVariable Long fromId) {
        return getFollowMemberFacade.execute(fromId);
    }
}
