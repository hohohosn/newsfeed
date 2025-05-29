package com.example.newsfeed.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
    private String author;  // 추후에 User 객체로 대체 가능
}

//지금까지 한 거: 글 등록, 수정, 응답