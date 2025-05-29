package com.example.newsfeed.domain.post.service;

import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.post.dto.PostCreateRequestDto;
import com.example.newsfeed.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // CREATE
    public Post createPost(PostCreateRequestDto request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .build();
        return postRepository.save(post);
    }

    // READ (전체)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // READ (구역)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시글이 없습니다: " + id));
    }

    // UPDATE
    public Post updatePost(Long id, PostCreateRequestDto request) {
        Post post = getPostById(id);
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(request.getAuthor());
        return postRepository.save(post);
    }

    /* DELETE (소프트 딜리트 추가 중)
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

     */
}
