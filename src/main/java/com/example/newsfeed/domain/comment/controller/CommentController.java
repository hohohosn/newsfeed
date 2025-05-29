package com.example.newsfeed.domain.comment.controller;

import com.example.newsfeed.domain.comment.dto.CommentResponseDto;
import com.example.newsfeed.domain.comment.dto.CreateCommentRequestDto;
import com.example.newsfeed.domain.comment.dto.FindAllCommentResponseDto;
import com.example.newsfeed.domain.comment.dto.UpdateCommentRequestDto;
import com.example.newsfeed.domain.comment.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> saveComment(
            @RequestBody CreateCommentRequestDto requestDto
            ) {
        Long commentId = commentService.saveComment(
                requestDto.getPostId(),
                requestDto.getContent()
        );

        URI location = URI.create("/comments/" + commentId);

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateCommentById(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequestDto requestDto
            ) {
        commentService.updateCommentById(commentId, requestDto);

        URI location = URI.create("/comments/" + commentId);

        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(location).build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> deleteCommentById(
            @PathVariable Long commentId
    ) {
        commentService.deleteCommentById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FindAllCommentResponseDto>> findAllComment() {
        List<FindAllCommentResponseDto> findAllCommentResponseDtoList = commentService.findAllComment();
        return new ResponseEntity<>(findAllCommentResponseDtoList, HttpStatus.OK);
    }
}
