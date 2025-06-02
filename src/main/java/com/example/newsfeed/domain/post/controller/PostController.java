package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.post.dto.PostResponseDto;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import com.example.newsfeed.domain.post.dto.PostUpdateRequestDto;

import com.example.newsfeed.domain.post.service.PostService;
import com.example.newsfeed.domain.user.entity.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostCreateRequestDto requestDto, HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        postService.createPost(requestDto, loginUser);
        return ResponseEntity.ok().build();
    }

    // 전체 게시글 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(
            @PageableDefault() Pageable pageable
    ) {
        Page<PostResponseDto> posts = postService.getPosts(pageable);
        return ResponseEntity.ok(posts);
    }


    // 단일 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @RequestBody PostUpdateRequestDto request,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        postService.updatePost(id, request, loginUser);
        return ResponseEntity.noContent().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @SessionAttribute(name = "loginUser", required = false) User loginUser) {

        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        postService.deletePost(id, loginUser);
        return ResponseEntity.noContent().build();
    }

    //뉴스피드에 내가 팔로우한 사람들 게시글이 최신순으로 노출
    @GetMapping("/follower")
    public ResponseEntity<Page<PostResponseDto>> getFollowerPosts(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @SessionAttribute(name = "loginUser", required = false) User loginUser) {
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> posts = postService.getFollowerPosts(loginUser.getId(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDto>> searchPosts (@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                                              @PageableDefault(sort = "updatedAt",
                                                                      direction =  Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(postService.search(startDate,endDate,pageable));
    }

}
