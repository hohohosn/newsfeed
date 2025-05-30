package com.example.newsfeed.domain.user.entity;

import com.example.newsfeed.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE user_id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity {

    public User(String email, String name, String password, String phoneNumber, LocalDate birth) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.isDeleted = false;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private LocalDate birth;
    private Boolean isDeleted;

    // 내가 친구추가한 사람들
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Friendship> following = new HashSet<>();


    // 나를 친구추가한 사람들
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
    private Set<Friendship> follower = new HashSet<>();



    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }


    public void updateProfile(String name, String phoneNumber, LocalDate birth) {
        Optional.ofNullable(name).ifPresent(n -> this.name = n);
        Optional.ofNullable(phoneNumber).ifPresent(pn -> this.phoneNumber = pn);
        Optional.ofNullable(birth).ifPresent(b -> this.birth = b);
    }

    public List<User> getFollowingList() {
        return following.stream().map(Friendship::getFriend).toList();
    }

    public List<User> getFollowerList() {
        return follower.stream().map(Friendship::getUser).toList();
    }
}
