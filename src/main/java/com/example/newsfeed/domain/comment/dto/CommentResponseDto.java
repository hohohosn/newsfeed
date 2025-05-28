package com.example.newsfeed.domain.comment.dto;

public class CommentResponseDto {
    private final Long postId;
    private final String content;

    public CommentResponseDto(Long postId, String content) {
        this.postId = postId;
        this.content = content;
    }
}
