package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<Post> getPosts(Long memberId, Pageable pageable) {
    // public Page<Post> getPosts(Long memberId, PageRequest request) {
        return postRepository.findAllByMemberId(memberId, pageable);
    }
}
