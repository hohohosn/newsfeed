package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.LoginRequestDto;
import com.example.newsfeed.domain.user.dto.LoginResponseDto;
import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequestDto));
    }

    // 회원 탈퇴
    @DeleteMapping("/{userid}")
    public ResponseEntity<Void> withdrawal(@PathVariable Long userid){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<LoginResponseDto> signin (@RequestBody LoginRequestDto loginRequestDto,
                                                    HttpServletRequest request){
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", userSevice.getUser(loginRequestDto));

        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        HttpSession session = request.getSession();

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
    @PatchMapping("/{userid}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userid,
                                                      @RequestBody UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userid,userRequestDto));
    }

    // 비밀번호 수정
    @PatchMapping("/{userid}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userid,
                                               @RequestParam String oldPassword,
                                               @RequestParam String newPassword){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePassword(userid,oldPassword,newPassword));
    }
    
    // 친구 추가
    @PostMapping("/{userid}/friendship")
    public ResponseEntity<Void> addFriend(@PathVariable Long userid,
                                          @RequestParam Long friendId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.addFriend(userid,friendId));
    }
    
    // 친구 삭제
    @DeleteMapping("/{userid}/friendship")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long userid,
                                             @RequestParam Long friendId){
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteFriend(userid,friendId));
    }
}
