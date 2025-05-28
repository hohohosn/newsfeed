package com.example.newsfeed.domain.user.repository;

import com.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(Long id);

    void remove(User user);
}
