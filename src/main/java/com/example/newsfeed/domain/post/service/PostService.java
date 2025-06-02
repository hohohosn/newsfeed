package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.post.dto.*;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    // create
    public void createPost(PostCreateRequestDto request, User loginUser) {

        Post post = new Post(
                request.getTitle(),
                loginUser,
                request.getContent()
        );
        postRepository.save(post);
    }


    // READ 전체 조회

    public Page<PostResponseDto> getPosts(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return postRepository.findAll(sortedPageable).map(post -> new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),    // User 엔티티의 이름을 꺼내서 전달
                post.getCreatedAt()));
    }

    // READ 단건 조회
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
    public void updatePost(Long id, PostUpdateRequestDto request, User loginUser) {
        Post post = findByIdOrElseThrow(id);
        if (!post.getUser().getId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        post.update(request.getTitle(), request.getContent());
    }

    //DELETE
    public void deletePost(Long id, User loginUser) {
        Post post = findByIdOrElseThrow(id);

        if (!post.getUser().getId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }
        postRepository.deleteById(id);
    }

    //뉴스피드 최신순 정렬
    public Page<PostResponseDto> getFollowerPosts(Long userId, Pageable pageable) {
        return postRepository.findFollowerPosts(userId, pageable).map(post -> new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),    // User 엔티티의 이름을 꺼내서 전달
                post.getCreatedAt()));
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


    public Page<PostResponseDto> search(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable){

        return postRepository.searchPosts(startDate,endDate,pageable).map(post
                -> new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getCreatedAt()
        ));
    }
}
