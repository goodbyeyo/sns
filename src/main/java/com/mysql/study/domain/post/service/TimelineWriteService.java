package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.entity.Timeline;
import com.mysql.study.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {

    private final TimelineRepository timelineRepository;

    public void deliveryToTimeline(Long postId, List<Long> toMemberIds) {
        var timelines = toMemberIds.stream()
                .map(memberId -> toTimeline(postId, memberId))
                .toList();

        timelineRepository.bulkInsert(timelines);
        // timelines.forEach(timelineRepository::save);
    }

    private Timeline toTimeline(Long postId, Long memberId) {
        return Timeline.builder()
                .memberId(memberId)
                .postId(postId)
                .build();
    }
}
