package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.LikeCommentResponseDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentLikeController {
    private final CommentService commentService;

    private User getLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return loginUser;
    }

    @GetMapping("/{commentId}/like")
    public ResponseEntity<LikeCommentResponseDto> showLikeAtCommentId(
            @PathVariable Long commentId
    ) {
        LikeCommentResponseDto likeCommentResponseDto =  commentService.showLikeAtCommentId(commentId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<LikeCommentResponseDto> addLikeAtCommentId(
            @PathVariable Long commentId,
            HttpSession session
    ) {
        User loginUser = getLoginUser(session);

        LikeCommentResponseDto likeCommentResponseDto =  commentService.addLikeAtCommentId(loginUser.getId(), commentId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<LikeCommentResponseDto> deleteLikeAtCommentId(
            @PathVariable Long commentId,
            HttpSession session
    ) {
        User loginUser = getLoginUser(session);

        LikeCommentResponseDto likeCommentResponseDto =  commentService.deleteLikeAtCommentID(loginUser.getId(), commentId);
        return new ResponseEntity<>(likeCommentResponseDto, HttpStatus.OK);
    }
}