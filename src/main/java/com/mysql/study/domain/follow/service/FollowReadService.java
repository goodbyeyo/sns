package com.mysql.study.domain.follow.service;

import com.mysql.study.domain.follow.dto.FollowDto;
import com.mysql.study.domain.follow.entity.Follow;
import com.mysql.study.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowReadService {

    private final FollowRepository followRepository;

    public List<FollowDto> getFollowings(Long fromMemberId) {
        return followRepository.findAllFromMemberId(fromMemberId)
                .stream()
                .map(FollowDto::of)
                .collect(Collectors.toList());
    }

    public List<FollowDto> getFollowers(Long toMemberId) {
        return followRepository.findAllToMemberId(toMemberId)
                .stream()
                .map(FollowDto::of)
                .collect(Collectors.toList());
    }

}
