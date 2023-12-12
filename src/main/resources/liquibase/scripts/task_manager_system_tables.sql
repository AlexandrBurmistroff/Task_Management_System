-- liquibase formatted sql

--changeset balex:create-user_entity_table
CREATE TABLE users
(
    id       bigserial PRIMARY KEY,
    username text,
    email    text,
    password text,
    task_id  begint
);

--changeset balex:create-task_entity_table
CREATE TABLE tasks
(
    id          bigserial PRIMARY KEY,
    title       text,
    description text,
    status      text,
    priority    text,
    creator_id  begint,
    executor_id begint
);

--changeset balex:create-comment_entity_table
CREATE TABLE comments
(
    id      bigserial PRIMARY KEY,
    comment text,
    task_id begint,
    user_id begint
);
