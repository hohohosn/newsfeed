package com.example.newsfeed.domain.post;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
@Getter
public class Post extends BaseEntity {

    public Post(String content, User user, String title) {
        this.content = content;
        this.user = user;
        this.like = 0L;
        this.title = title;
        this.isDeleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    private Long like;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



}
