package com.example.newsfeed.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikePostResponseDto {
    private Long postId;
    private Long likes;
}