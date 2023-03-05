package com.mysql.study.domain.post.dto;

import com.mysql.study.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String content,
        LocalDateTime createdAt,
        Long likeCount
) {
}
