package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostWriteService {

    private final PostRepository postRepository;

    public Long create(PostCommand command) {
        var post = Post.builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();
        return postRepository.save(post).getId();
    }

    @Transactional // 잠금획득
    public void likePost(Long postId) {
        var post = postRepository.findById(postId, true).orElseThrow();
        post.increaseLikeCount();
        postRepository.update(post);
    }

    public void likePostWithOptimisticLock(Long postId) {   // 낙관적 락
        var post = postRepository.findById(postId, false).orElseThrow();
        post.increaseLikeCount();
        postRepository.update(post);
    }
}
