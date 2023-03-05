package com.mysql.study.domain.post.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Post {

    private final Long id;
    private final Long memberId;
    private final String contents;
    private final LocalDate createdDate;
    private final LocalDateTime createdAt;
    private Long likeCount;
    private final Long version;

    @Builder
    public Post(Long id, Long memberId, String contents, LocalDate createdDate, LocalDateTime createdAt, Long likeCount, Long version) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.likeCount = likeCount == null ? 0L : likeCount;
        this.version = version == null ? 0L : version;
    }

    public void increaseLikeCount() {
        likeCount += 1;
    }
}
