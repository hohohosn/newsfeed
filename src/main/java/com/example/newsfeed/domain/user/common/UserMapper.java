package com.example.newsfeed.domain.user.common;

import com.example.newsfeed.domain.user.dto.UserFollowResponseDto;
import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;

import java.util.List;

public class UserMapper {
    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserName(user.getName());
        dto.setBirth(user.getBirth());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    //TODO 친구도 페이징으로 가져와야 할것 같음... 일단 전체 조회로 구현
    public static UserFollowResponseDto toFollowResponseDto(User user) {
        List<Long> followingIdList = user.getFollowingList().stream().map(User::getId).toList();
        List<Long> followerIdList = user.getFollowerList().stream().map(User::getId).toList();
        UserFollowResponseDto dto = new UserFollowResponseDto();
        dto.setFollowing(followingIdList);
        dto.setFollower(followerIdList);
        dto.setFollowingCount((long) followingIdList.size());
        dto.setFollowerCount((long) followerIdList.size());
        return dto;
    }

    public static User toEntity(UserRequestDto dto){
        return new User(dto.getEmail(), dto.getUserName(), dto.getPassword(), dto.getPhoneNumber(),dto.getBirth());
    }


}
