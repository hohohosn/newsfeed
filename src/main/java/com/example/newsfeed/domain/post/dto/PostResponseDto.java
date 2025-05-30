package com.example.newsfeed.domain.post.dto;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;

    public PostResponseDto(Long id, String title, String content, String userName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = userName;
        this.createdAt = createdAt;
    }
}
