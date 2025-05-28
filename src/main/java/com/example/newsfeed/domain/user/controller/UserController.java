package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.LoginRequestDto;
import com.example.newsfeed.domain.user.dto.LoginResponseDto;
import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody UserRequestDto userRequestDto){
        Long id = userService.signup(userRequestDto);
        //location 설정
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}").buildAndExpand(id).toUri();
        // 리다이렉트 PRG
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(uri).build();
    }

    // 회원 탈퇴
    @DeleteMapping("/{userid}")
    public ResponseEntity<Void> withdrawal(@PathVariable Long userid, HttpServletRequest request){
        userService.withdrawal(userid);
        HttpSession session = request.getSession(false);

        // 세션 삭제
        if (session != null) {session.invalidate();}
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDto> signin (@RequestBody LoginRequestDto loginRequestDto,
                                                    HttpServletRequest request){
        User user = userService.login(loginRequestDto);

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{userId}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).location(uri).build();
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 프로필 조회
    @GetMapping("/{userid}")
    public ResponseEntity<UserResponseDto> selectUser(@PathVariable Long userid){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(userid));
    }

    // 프로필 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userId,userRequestDto));
    }

    // 비밀번호 수정
    @PatchMapping("/{userid}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userid,
                                               @RequestParam String oldPassword,
                                               @RequestParam String newPassword){
        userService.updatePassword(userid, oldPassword, newPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 친구 추가
    @PostMapping("/{userid}/friendship")
    public ResponseEntity<Void> addFriend(@PathVariable Long userid,
                                          @RequestParam Long friendId){
        userService.addFriend(userid, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 친구 삭제
    @DeleteMapping("/{userid}/friendship")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long userid,
                                             @RequestParam Long friendId){
        userService.deleteFriend(userid, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
