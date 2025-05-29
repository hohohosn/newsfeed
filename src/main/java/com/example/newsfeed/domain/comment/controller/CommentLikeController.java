package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.LikeCommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentLikeController {
    private final CommentService commentService;

    @GetMapping("/{commentId}/like")
    public ResponseEntity<LikeCommentResponseDto> showLikeAtCommentId(
            @PathVariable Long commentId
    ) {
        LikeCommentResponseDto likeCommentResponseDto =  commentService.showLikeAtCommentId(commentId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<LikeCommentResponseDto> addLikeAtCommentId(
            @PathVariable Long commentId
    ) {
        LikeCommentResponseDto likeCommentResponseDto =  commentService.addLikeAtCommentId(commentId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.OK);
    }
}
