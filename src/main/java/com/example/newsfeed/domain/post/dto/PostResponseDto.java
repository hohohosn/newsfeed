package com.example.newsfeed.domain.post.dto;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
}
