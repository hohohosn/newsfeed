package com.example.newsfeed.domain.comment.entity;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.domain.post.entity.Post;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@Getter
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE comment_id = ?")
@SQLRestriction("is_deleted = false")
public class Comment extends BaseEntity{

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.likes = 0L;
        this.isDeleted = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private Long likes;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    private Post post;

    public void updateComment(String content) {
        Optional.ofNullable(content).ifPresent(n -> this.content = n);
    }

    public void addLike() {
        ++this.likes;
    }

    public void deleteLike() {
        --this.likes;
    }
}
