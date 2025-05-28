package com.example.newsfeed.domain.user.dto;

import com.example.newsfeed.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserRequestDto {
    private String email;
    private String password;
    private String phoneNumber;
    private String userName;
    private LocalDate birth;
    private boolean isDeleted;

    public User toEntity(){
        return new User(email,password,phoneNumber,userName,birth);
    }
}
