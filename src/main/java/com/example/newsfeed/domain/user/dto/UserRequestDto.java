package com.example.newsfeed.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserRequestDto {
    @Email(message = "올바른 형식의 이메일 주소를 입력해주세요")
    private String email;

    private String password;

    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호 숫자는 10~11자리입니다.")
    private String phoneNumber;

    private String userName;

    @PastOrPresent(message = "[과거 또는 현재의 날짜여야 합니다")
    private LocalDate birth;



}
