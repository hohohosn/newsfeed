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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // create
    public Long createPost(PostCreateRequestDto request, User loginUser) {

        Post post = new Post(
                request.getTitle(),
                loginUser,
                request.getContent()
        );
        return postRepository.save(post).getId();
    }


    // READ 전체 조회

    public Page<Post> getPosts(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return postRepository.findAll(sortedPageable);
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
