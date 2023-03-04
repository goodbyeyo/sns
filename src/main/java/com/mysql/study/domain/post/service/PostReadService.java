package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.dto.DailyPostCount;
import com.mysql.study.domain.post.dto.DailyPostCountRequest;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.repository.PostRepository;
import com.mysql.study.util.CursorRequest;
import com.mysql.study.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return postRepository.findAllByMemberId(memberId, pageable);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {

        var posts = findAllByMemberId(memberId, cursorRequest);
        // Optional<Long> min = posts.stream().map(Post::getId).min(Long::compareTo);
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private List<Post> findAllByMemberId(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasNext()) {
            return postRepository.findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(
                    memberId, cursorRequest.key(), cursorRequest.size());
        }
        return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
        var posts = findAllByInMemberId(memberIds, cursorRequest);
        // Optional<Long> min = posts.stream().map(Post::getId).min(Long::compareTo);
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private List<Post> findAllByInMemberId(List<Long> memberIds, CursorRequest cursorRequest) {
        if (cursorRequest.hasNext()) {
            return postRepository.findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(
                    memberIds, cursorRequest.key(), cursorRequest.size());
        }
        return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());
    }

    private long getNextKey(List<Post> posts) {
        return posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

    public List<Post> getPosts(List<Long> ids) {
        return postRepository.findAllByInId(ids);
    }
}
