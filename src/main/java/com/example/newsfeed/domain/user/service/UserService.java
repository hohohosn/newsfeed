package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.common.exception.PasswordMismatchException;
import com.example.newsfeed.domain.user.common.PasswordEncoder;
import com.example.newsfeed.domain.user.common.UserMapper;
import com.example.newsfeed.domain.user.dto.LoginRequestDto;
import com.example.newsfeed.domain.user.dto.LoginResponseDto;
import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.dto.UserResponseDto;
import com.example.newsfeed.domain.user.entity.Friendship;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.FriendshipRepository;
import com.example.newsfeed.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FriendshipRepository friendshipRepository;
    //회원가입 signup
    @Transactional
    public Long signup(UserRequestDto dto) {
        User user = UserMapper.toEntity(dto);

        //비밀번호 암호화
        user.setEncodedPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.findByEmail(dto.getEmail()).ifPresent(u -> {throw new IllegalArgumentException("이메일이 이미 존재합니다.");});

        userRepository.save(user);

        return user.getId();
    }
    // 회원탈퇴
    @Transactional
    public void withdrawal(Long id) {
        User user = findById(id);
        user.delete();
    }

    public User login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new PasswordMismatchException("계정이 올바르지 않습니다."));
        //탈퇴한 회원인지 확인
        checkIsDelete(user);
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 올바르지 않습니다.");
        }

        return user;
    }

    public UserResponseDto getProfile(Long id) {
        User user = findById(id);

        //탈퇴한 회원인지 확인
        checkIsDelete(user);

        if(user.getIsDeleted()) throw new IllegalStateException("이미 탈퇴한 회원입니다");
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateProfile(Long id, UserRequestDto dto) {
        User user = findById(id);
        user.updateProfile(dto.getUserName(), dto.getPhoneNumber(), dto.getBirth());
        return UserMapper.toResponseDto(user);
    }
    @Transactional
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = findById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordMismatchException("비밀번호가 올바르지 않습니다.");
        }

        user.setEncodedPassword(passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void addFriend(Long id, Long friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        friendshipRepository.save(new Friendship(user, friend));
    }

    @Transactional
    public void deleteFriend(Long id, Long friendId) {
        User user = findById(id);
        User friend = findById(friendId);
        Friendship friendship = friendshipRepository.findByUserAndFriend(user, friend).orElseThrow(() -> new EntityNotFoundException("유저(id:" + id + ")와 유저(id:" + id + ")의 친구 관계가 없습니다."));
        friendshipRepository.delete(friendship);
    }

    private User findById(Long id) {
       return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("유자(id:" + id + ")가 존재하지 않습니다."));
    }

    private void checkIsDelete(User user) {
        if(user.getIsDeleted()) throw new IllegalStateException("이미 탈퇴한 회원입니다");
    }
}
