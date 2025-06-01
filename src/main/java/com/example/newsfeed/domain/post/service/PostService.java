package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.post.dto.LikePostResponseDto;
import com.example.newsfeed.domain.post.dto.PostResponseDto;
import com.example.newsfeed.domain.post.dto.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // create
    public Long createPost(PostCreateRequestDto request, User loginUser) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("유저(id:" + request.getUserId() + ")가 존재하지 않습니다."));

        Post post = new Post(
                request.getContent(),
                user, // 유저 주입 해야함
                request.getTitle());
        return postRepository.save(post).getId();
    }


    // READ (전체)
//    public Page<PostResponseDto> getAllPosts() {
//        return postRepository.findAllByIsDeletedFalse(pageable)
//                .map(PostResponseDto::new);
//    }

    // READ (구역) 단건 조회
    public PostResponseDto getPostById(Long id) {
        Post post = findByIdOrElseThrow(id);
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getCreatedAt()
        );
    }

    // UPDATE
    public void updatePost(Long id, PostUpdateRequestDto request) {
        Post post = postRepository.findByIdOrElseThrow(id);
        post.update(request.getTitle(), request.getContent());
    }

    //DELETE
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public LikePostResponseDto addLikeAtPostId(Long postId) {
        Post findPost = postRepository.findByIdOrElseThrow(postId);
        findPost.addLike();

        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikes()
        );
    }

    public LikePostResponseDto showLikeAtPostId(Long postId) {
        Post findPost = postRepository.findByIdOrElseThrow(postId);
        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikes()
        );
    }

    public void deleteLikeAtPostId(Long postId) {
        Post findPost = postRepository.findByIdOrElseThrow(postId);
        findPost.deleteLike();
    }

    private Post findByIdOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + postId));
    }

}
