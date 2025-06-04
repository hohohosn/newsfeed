package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.post.dto.*;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.repository.PostRepository;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

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


    //좋아요 추가
    public LikePostResponseDto addLikeAtPostId(Long postId, User loginUser) {
        Post findPost = postRepository.findByIdOrElseThrow(postId);

        if (findPost.getUser().getId().equals(loginUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 게시물에는 좋아요를 누를 수 없습니다.");
        }

        // 중복 체크 + 좋아요 반영
        findPost.addLike(loginUser);

        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikes()
        );
    }


    //좋아요 조회
    public LikePostResponseDto showLikeAtPostId(Long postId) {
        Post findPost = postRepository.findByIdOrElseThrow(postId);
        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikes()
        );
    }

    public LikePostResponseDto deleteLikeAtPostId(Long userId, Long postId) {
        Post findPost = postRepository.findByIdOrElseThrow(postId); // ✅ 타입 및 변수명 수정

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        findPost.deleteLike(findUser); // ✅ 중복 체크 포함된 메서드

        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikes()
        );
    }


    private Post findByIdOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + postId));
    }

}
