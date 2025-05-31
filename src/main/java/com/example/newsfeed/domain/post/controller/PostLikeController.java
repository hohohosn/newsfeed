package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.post.dto.LikePostResponseDto;
import com.example.newsfeed.domain.post.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostLikeController {
    private final PostService postService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikePostResponseDto> addLikeAtPostId(
            @PathVariable Long postId
    ) {
        LikePostResponseDto likePostResponseDto =
                postService.addLikeAtPostId(postId);
        return new ResponseEntity<>(likePostResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{postId}/like")
    public ResponseEntity<LikePostResponseDto> showLikeAtPostId(
            @PathVariable Long postId
    ) {
        LikePostResponseDto likePostResponseDto =
                postService.showLikeAtPostId(postId);
        return new ResponseEntity<>(likePostResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> deleteLikeAtPostId(
            @PathVariable Long postId
    ) {
        postService.deleteLikeAtPostId(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
