package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findByIdOrElseThrow(Long postId) {
        return findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + postId));
    }
}

