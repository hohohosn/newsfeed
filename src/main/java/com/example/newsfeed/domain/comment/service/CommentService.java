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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Long saveComment(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Does not exist id = " + postId));

        User user = userRepository.findById(post.getUser().getId()).orElseThrow(
                () -> new IllegalArgumentException("Does not exist id = " + post.getUser().getId()));

        Comment comment = new Comment(post, user, content);
        commentRepository.save(comment);

        return comment.getId();
    }

    public void updateCommentById(Long userId, Long commentId, UpdateCommentRequestDto requestDto) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        if (!findComment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 수정 권한이 없습니다.");
        }
        findComment.updateComment(requestDto.getContent());
    }

    public void deleteCommentById(Long userId, Long commentId) { // hard delete
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);

        Long commentOwnerId = findComment.getUser().getId();
        Long postOwnerId = findComment.getPost().getUser().getId();

        if (!userId.equals(commentOwnerId) && !userId.equals(postOwnerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글 삭제 권한이 없습니다.");
        }

        commentRepository.deleteById(commentId);
    }


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

    public LikeCommentResponseDto addLikeAtCommentId(Long userId, Long commentId) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        if (findComment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글에 좋아요를 할 수 없습니다.");
        }
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        findComment.addLike(findUser);

        return new LikeCommentResponseDto(
                findComment.getId(),
                findComment.getLikes()
        );
    }

    public LikeCommentResponseDto deleteLikeAtCommentID(Long userId, Long commentId) {
        Comment findComment = commentRepository.findByIdOrElseThrow(commentId);
        if (findComment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 댓글에 좋아요를 할 수 없습니다.");
        }

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        findComment.deleteLike(findUser);

        return new LikeCommentResponseDto(
                findComment.getId(),
                findComment.getLikes()
        );
    }
}
