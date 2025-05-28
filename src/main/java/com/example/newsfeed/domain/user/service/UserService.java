package com.example.newsfeed.domain.user.service;

import com.example.newsfeed.domain.user.common.PasswordEncoder;
import com.example.newsfeed.domain.user.dto.UserRequestDto;
import com.example.newsfeed.domain.user.entity.User;
import com.example.newsfeed.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입 signup
    public Long save(UserRequestDto dto) {
        User user = dto.toEntity();

        //비밀번호 암호화
        user.setEncodedPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        return user.getId();
    }

}
