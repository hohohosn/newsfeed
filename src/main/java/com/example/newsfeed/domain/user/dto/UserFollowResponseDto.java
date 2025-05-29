package com.example.newsfeed.domain.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserFollowResponseDto {
    private Long followingCount;
    private List<Long> following;
    private Long followerCount;
    private List<Long> follower;
}
