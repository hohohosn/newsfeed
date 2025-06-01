package com.example.newsfeed.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
}
