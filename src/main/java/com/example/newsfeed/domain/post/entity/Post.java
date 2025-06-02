package com.example.newsfeed.domain.post.entity;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE post_id = ?")
@SQLRestriction("is_deleted = false")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likes = 0L;

    @Column(nullable = false)
    private boolean isDeleted = false;

    //유저랑 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_like_users",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private Set<User> likedUsers = new HashSet<>();

    //좋아요 추가
    public void addLike(User user) {
        if (!likedUsers.add(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다.");
        }
        this.likes++;
    }

    //좋아요 취소
    public void deleteLike(User user) {
        if (!likedUsers.remove(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않았습니다.");
        }
        this.likes--;
    }


    public void update(String title, String content) {
        Optional.ofNullable(title).ifPresent(n -> this.title = n);
        Optional.ofNullable(content).ifPresent(n -> this.content = n);
    }

    public Post(String title, User user, String content) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

}
