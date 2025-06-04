package com.example.newsfeed.domain.post.dto;

import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;
}
