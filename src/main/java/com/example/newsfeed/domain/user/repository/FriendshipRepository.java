package com.example.newsfeed.domain.user.repository;

import com.example.newsfeed.domain.user.entity.Friendship;
import com.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByUserAndFriend(User user, User friend);

    boolean existsByUserAndFriend(User user, User friend);
}
