package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.post.service.PostService;
import com.example.newsfeed.domain.post.service.PostService;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostCreateRequestDto request) {
        postService.createPost(request);
        return ResponseEntity.ok().build();
    }
}
