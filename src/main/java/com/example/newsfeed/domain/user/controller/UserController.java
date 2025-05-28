package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

}
