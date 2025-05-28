package com.example.newsfeed.domain.comment.dto;

import com.example.newsfeed.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindAllCommentResponseDto {
    private final Long commentId;
    private final String content;
    private final Long like;
    private final String author;
    private final LocalDateTime createAt;
    private final LocalDateTime updateAt;

    public FindAllCommentResponseDto(Long commentId, String content, Long like, String author, LocalDateTime createAt, LocalDateTime updateAt) {
        this.commentId = commentId;
        this.content = content;
        this.like = like;
        this.author = author;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static FindAllCommentResponseDto toDto (Comment commet) {
        return new FindAllCommentResponseDto(
                commet.getId(),
                commet.getContent(),
                commet.getLike(),
                commet.getUser().getName(),
                commet.getCreatedAt(),
                commet.getUpdatedAt()
        );
    }
}
