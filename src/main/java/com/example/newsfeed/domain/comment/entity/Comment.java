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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "comment_like_users",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> likedUsers = new HashSet<>();

    public void addLike(User user) {
        if (!likedUsers.add(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 좋아요를 누르셨습니다.");
        }
        this.likes++;
    }

    public void deleteLike(User user) {
        if (!likedUsers.remove(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않았습니다.");
        }
        this.likes--;
    }
}
