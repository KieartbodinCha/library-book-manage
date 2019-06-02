create table books
(
    id          bigint               not null primary key,
    author_name varchar(255)         null,
    book_name   varchar(255)         null,
    price       bigint     default 0 null,
    recommended tinyint(1) default 0 null
);

create table users
(
    id         bigint auto_increment primary key,
    username   varchar(100) not null,
    password   varchar(255) not null,
    birth_date date         null,
    role_name  varchar(20)  null
);

create table books_order
(
    id         bigint auto_increment primary key,
    book_id    bigint not null,
    user_id    bigint not null
);

create table users_login_log
(
    id         bigint auto_increment primary key,
    username      varchar(100) not null,
    login_date    datetime     null,
    login_status  varchar(50)  null,
    login_message text         null
);

create index users_login_log_user_id_index
    on users_login_log (username);


