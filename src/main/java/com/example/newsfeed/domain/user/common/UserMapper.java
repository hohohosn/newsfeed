package com.example.newsfeed.domain.user.common;

import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;

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


    public static User toEntity(UserRequestDto dto){
        return new User(dto.getEmail(), dto.getUserName(), dto.getPassword(), dto.getPhoneNumber(),dto.getBirth());
    }
}
