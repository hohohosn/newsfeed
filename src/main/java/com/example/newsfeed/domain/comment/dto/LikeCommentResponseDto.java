package com.example.newsfeed.domain.comment.dto;

import lombok.Getter;

@Getter
public class LikeCommentResponseDto {
    private final Long commentId;
    private final Long like;

    public LikeCommentResponseDto(Long commentId, Long like) {
        this.commentId = commentId;
        this.like = like;
    }
}
