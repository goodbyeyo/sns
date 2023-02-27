package com.mysql.study.domain.post.dto;

public record PostCommand(Long memberId, String contents) {
}
