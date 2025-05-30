package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.post.dto.PostResponseDto;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import com.example.newsfeed.domain.post.dto.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostCreateRequestDto requestDto) {
        Long postId = postService.createPost(requestDto);
        URI location = URI.create("/posts/" + postId);
        return ResponseEntity.created(location).build();
    }

    // 전체 게시글 조회 (페이징)
//    @GetMapping
//    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
//        return ResponseEntity.ok(postService.getAllPosts(pageable));
//    }

    // 단일 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto request) {
        postService.updatePost(id, request);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
