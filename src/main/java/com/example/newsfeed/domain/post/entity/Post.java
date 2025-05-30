package com.example.newsfeed.domain.post.entity;

import com.example.newsfeed.common.BaseEntity;
import com.example.newsfeed.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedPostFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedPostFilter", condition = "is_deleted = :isDeleted")

public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long likeCount = 0L;


    private String author;


    @Column(updatable = false)
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    //유저랑 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


   //소프트 딜리트 추가 중

   @Column(nullable = false)
   private boolean isDeleted = false;

    public void addLike() {
        ++this.likeCount;
    }


}
