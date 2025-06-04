package com.example.newsfeed.domain.post.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String userId;
    private LocalDateTime createdAt;

    public PostResponseDto(Long id, String title, String content, String userName, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userName;
        this.createdAt = createdAt;
    }
}
