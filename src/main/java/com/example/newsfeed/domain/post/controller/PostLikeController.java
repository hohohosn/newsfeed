package com.example.newsfeed.domain.post.controller;

import com.example.newsfeed.domain.post.dto.LikePostResponseDto;
import com.example.newsfeed.domain.post.service.PostService;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostLikeController {
    private final PostService postService;

    private User getLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return loginUser;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<LikePostResponseDto> addLikeAtPostId(
            @PathVariable Long postId,
            @SessionAttribute(name = "loginUser") User loginUser
    ) {
        LikePostResponseDto likePostResponseDto =
                postService.addLikeAtPostId(postId, loginUser);
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
    public ResponseEntity<LikePostResponseDto> deleteLikeAtPostId(
            @PathVariable Long postId,
            HttpSession session
    ) {
        User loginUser = getLoginUser(session);

        LikePostResponseDto likePostResponseDto = postService.deleteLikeAtPostId(loginUser.getId(),postId);
        return new ResponseEntity<>(likePostResponseDto,HttpStatus.OK);
    }
}
