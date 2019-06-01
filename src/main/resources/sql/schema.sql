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
    session_id varchar(255) null,
    role_name  varchar(20)  null
);


