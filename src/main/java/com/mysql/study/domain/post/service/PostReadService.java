package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {

    private final PostRepository postRepository;

    public List<DailyPostCount> getDairyPostCount(DailyPostCountRequest request) {
        // 작성일자, 작성회원, 작성 게시물 개수 조회
        return postRepository.groupByCreatedDate(request);
    }
}
