package com.example.newsfeed.domain.post.entity;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.Optional;


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

    //유저랑 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(nullable = false)
    private boolean isDeleted = false;

    public void addLike() {
        ++this.likes;
    }

    public void deleteLike() {
        --this.likes;
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
