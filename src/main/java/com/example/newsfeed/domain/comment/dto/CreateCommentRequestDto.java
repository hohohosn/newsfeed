package com.example.newsfeed.domain.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequestDto {
    private final Long postId;
    private final String content;

    public CreateCommentRequestDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
