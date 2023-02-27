package com.mysql.study.domain.post.service;

import com.mysql.study.domain.post.dto.PostCommand;
import com.mysql.study.domain.post.entity.Post;
import com.mysql.study.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
