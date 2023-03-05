package com.mysql.study.domain.post.service;

import com.mysql.study.domain.member.dto.MemberDto;
import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.entity.PostLike;
import com.mysql.study.domain.post.repository.PostLikeRepository;
import com.mysql.study.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;

    public Long create(Post post, MemberDto memberDto) {
        var postLike = PostLike.builder()
                .postId(post.getId())
                .memberId(memberDto.id())
                .build();
        return postLikeRepository.save(postLike).getId();
    }


}
