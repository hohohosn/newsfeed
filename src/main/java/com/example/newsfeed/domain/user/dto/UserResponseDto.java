package com.example.newsfeed.domain.user.dto;

import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long userId;
    private String email;
    private String phoneNumber;
    private String userName;
    private LocalDate birth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
