package com.example.newsfeed.domain.post.repository;

import com.example.newsfeed.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    default Post findByIdOrElseThrow(Long postId) {
        return findById(postId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다." + postId));
    }

    @Query("select p from Post p where p.user.id in (select f.friend.id from Friendship f where f.user.id = :userId) order by p.createdAt desc")
    Page<Post> findFollowerPosts(Long userId, Pageable pageable);

    @Query("select p from Post p where p.createdAt between :startDate and :endDate")
    Page<Post> searchPosts(@Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           Pageable pageable);
}
