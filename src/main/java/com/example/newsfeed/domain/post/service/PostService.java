package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.post.dto.LikePostResponseDto;
import com.example.newsfeed.domain.post.dto.PostResponseDto;
import com.example.newsfeed.domain.post.dto.PostUpdateRequestDto;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import com.example.newsfeed.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // create
    public Long createPost(PostCreateRequestDto request) {
        Post post = new Post()
        request.getContent(),
                request.getUser(),  // 유저 주입 해야함
                request.getTitle();
        return postRepository.save(post).getId();
    }


    // READ (전체)
    public Page<PostResponseDto> getAllPosts() {
        return postRepository.findAllByIsDeletedFalse(pageable)
                .map(PostResponseDto::new);
    }

    // READ (구역)
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        return new PostResponseDto(post);
    }

    // UPDATE

    public void updatePost(Long id, PostUpdateRequestDto request) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        post.update(request.getTitle(), request.getContent());
    }

    //DELETE (소프트 딜리트 추가 중)
    public void deletePost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        post.delete();
        postRepository.softDeleteById(id);
    }

    public LikePostResponseDto addLikeAtPostId(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Does not exist id = " + postId));
        findPost.addLike();

        return new LikePostResponseDto(
                findPost.getId(),
                findPost.getLikeCount()
        );
    }



}
