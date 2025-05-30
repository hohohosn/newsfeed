package com.example.newsfeed.domain.comment.service;

import com.example.newsfeed.domain.comment.dto.FindAllCommentResponseDto;
import com.example.newsfeed.domain.comment.dto.LikeCommentResponseDto;
import com.example.newsfeed.domain.comment.dto.UpdateCommentRequestDto;
import com.example.newsfeed.domain.comment.entity.Comment;
import com.example.newsfeed.domain.comment.repository.CommentRepository;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public Long saveComment(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Does not exist id = " + postId));
        User user = userRepository.findById(post.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("Does not exist id = " + post.getUser().getId()));
        Comment comment = new Comment((Entity) post, user, content);
        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void updateCommentById(Long commentId, UpdateCommentRequestDto requestDto) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        findComment.updateComment(requestDto.getContent());
    }

    @Transactional
    public void deleteCommentById(Long commentId) { // hard delete
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void softDeleteCommentById(Long commentId) {
        commentRepository.softDeleteById(commentId);
    }

    @Transactional
    public List<FindAllCommentResponseDto> findAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(FindAllCommentResponseDto::toDto).toList();
    }

    public LikeCommentResponseDto showLikeAtCommentId(Long commentId) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

        return new LikeCommentResponseDto(
                findComment.getId(),
                findComment.getLikes()
        );
    }

    public LikeCommentResponseDto addLikeAtCommentId(Long commentId) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        findComment.addLike();

        return new LikeCommentResponseDto(
                findComment.getId(),
                findComment.getLikes()
        );
    }
}
