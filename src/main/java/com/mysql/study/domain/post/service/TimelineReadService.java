package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.entity.Timeline;
import com.mysql.study.domain.post.repository.TimelineRepository;
import com.mysql.study.util.CursorRequest;
import com.mysql.study.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineReadService {

    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest) {
        var timelines = findAllByMemberId(memberId, cursorRequest);
        var nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllByMemberId(Long memberId, CursorRequest cursorRequest){
        if (cursorRequest.hasNext()) {
            return timelineRepository.findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(
                    memberId, cursorRequest.key(), cursorRequest.size());
        }
        return timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }
}
