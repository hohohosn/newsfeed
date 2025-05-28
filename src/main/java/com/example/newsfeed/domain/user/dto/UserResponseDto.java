package com.example.newsfeed.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String email;
    private String phoneNumber;
    private String userName;
    private LocalDate birth;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Long following;
    private Long follower;
}
