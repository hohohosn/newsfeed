create table users
(
    user_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(255) not null unique,
    password     char(60)     not null,
    phone_number varchar(20)  not null,
    name         varchar(255) not null,
    birth        date         not null,
    is_deleted   tinyint      not null default 0,
    created_at   timestamp    not null,
    updated_at   timestamp    not null
);

create table posts
(
    post_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      varchar(255) not null,
    content    text         not null,
    likes      bigint       not null default 0,
    is_deleted tinyint      not null default 0,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    user_id    bigint
);

create table comments
(
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    text      not null,
    likes      bigint    not null default 0,
    is_deleted tinyint   not null default 0,
    created_at timestamp not null,
    updated_at timestamp not null,
    post_id    bigint,
    user_id    bigint

);

create table friendships
(
    friendship_id bigint auto_increment primary key,
    user_id       bigint,
    friend_id     bigint
);

create table user_post_likes
(
    post_id bigint,
    user_id bigint
);

create table comment_like_users
(
    comment_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    PRIMARY KEY (comment_id, user_id)
);


alter table posts
    add foreign key (user_id) references users (user_id);
alter table comments
    add foreign key (post_id) references posts (post_id);
alter table comments
    add foreign key (user_id) references users (user_id);
alter table friendships
    add foreign key (user_id) references users (user_id);
alter table friendships
    add foreign key (friend_id) references users (user_id);
alter table user_post_likes
    add foreign key (post_id) references posts (post_id);
alter table user_post_likes
    add foreign key (user_id) references users (user_id);
alter table comment_like_users
    add foreign key (comment_id) references comments (comment_id);
alter table comment_like_users
    add foreign key (user_id) references users (user_id);