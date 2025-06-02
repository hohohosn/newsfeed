package com.example.newsfeed.domain.user.controller;

import com.example.newsfeed.domain.user.dto.*;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 접근 권한 확인
    private void verifyUserAccess(HttpSession session,Long userId){
        User loginUser = (User)session.getAttribute("loginUser");
        Long loginId = loginUser.getId();

        if(!loginId.equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"접근 권한이 존재하지 않습니다.");
        }
    }

    // 아이디와 친구아이디 관계
    private void checkUserEqualsFriend(Long userId, Long FriendId){
        if(userId.equals(FriendId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 요청입니다");
        }
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@Valid @RequestBody UserRequestDto userRequestDto){
        Long id = userService.signup(userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    // 회원 탈퇴
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> withdrawal(@PathVariable Long userId,
                                           HttpServletRequest request,
                                           HttpSession session){

        // 접근 권한 확인
        verifyUserAccess(session,userId);

        userService.withdrawal(userId);
        session = request.getSession(false);

        // 세션 삭제
        if (session != null) {session.invalidate();}
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<Long> signin (@Valid @RequestBody LoginRequestDto loginRequestDto,
                                                    HttpServletRequest request){
        User user = userService.login(loginRequestDto);

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);

        return ResponseEntity.status(HttpStatus.OK).body(user.getId());
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
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> selectUser(@PathVariable Long userId,
                                                      HttpSession session) {
        // 접근 권한 확인
        verifyUserAccess(session,userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getProfile(userId));
    }

    // 프로필 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody UserRequestDto userRequestDto,
                                                      HttpSession session){
        // 접근 권한 확인
        verifyUserAccess(session,userId);

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(userId,userRequestDto));
    }

    // 비밀번호 수정
    @PatchMapping("/{userId}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long userId,
                                               @Valid @RequestBody UserUpdatePasswordRequestDto requestDto,
                                               HttpSession session){
        // 접근 권한 확인
        verifyUserAccess(session,userId);


        userService.updatePassword(userId, requestDto.getOldPassword(), requestDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 친구 추가
    @PostMapping("/{userId}/friendship")
    public ResponseEntity<Void> addFriend(@PathVariable Long userId,
                                          @RequestParam Long friendId,
                                          HttpSession session){
        // userId, friendId 관계
        checkUserEqualsFriend(userId, friendId);

        // 접근 권한 확인
        verifyUserAccess(session,userId);

        userService.addFriend(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    // 친구 삭제
    @DeleteMapping("/{userId}/friendship")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long userId,
                                             @RequestParam Long friendId,
                                             HttpSession session){
        // userId, friendId 관계
        checkUserEqualsFriend(userId, friendId);

        // 접근 권한 확인
        verifyUserAccess(session,userId);

        userService.deleteFriend(userId, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // (팔로우, 팔로워) 수, 목록
    @GetMapping("/{userId}/follow")
    public ResponseEntity<UserFollowResponseDto> getUserFollow(@PathVariable Long userId,
                                                               HttpSession session) {

        // 접근 권한 확인
        verifyUserAccess(session,userId);

        UserFollowResponseDto dto = userService.getUserFollow(userId);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
