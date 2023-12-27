-- liquibase formatted sql

--changeset balex:create-users_entity_table
CREATE TABLE users
(
    id           bigserial PRIMARY KEY NOT NULL,
    username     varchar(155) NOT NULL,
    email        varchar(155) UNIQUE NOT NULL,
    password     varchar NOT NULL
);

--changeset balex:create-task_entity_table
CREATE TABLE task
(
    id          bigserial PRIMARY KEY NOT NULL,
    title       VARCHAR(155) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    status      VARCHAR(50) NOT NULL,
    priority    VARCHAR(50) NOT NULL,
    creator_id  bigserial NOT NULL,
    executor_id bigserial,
    CONSTRAINT creator_id_fk FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT executor_id_fk FOREIGN KEY (executor_id) REFERENCES users(id)
);

--changeset balex:create-comments_entity_table
CREATE TABLE comments
(
    id      bigserial PRIMARY KEY NOT NULL,
    comment VARCHAR(500) NOT NULL,
    task_id bigserial NOT NULL,
    author_id bigserial NOT NULL,
    created timestamp NOT NULL,
    CONSTRAINT task_id_fk FOREIGN KEY (task_id) REFERENCES task(id),
    CONSTRAINT author_id_fk FOREIGN KEY (author_id) REFERENCES users(id)
);

--changeset balex:create-role_entity_table
CREATE TABLE role
(
    id   bigserial PRIMARY KEY NOT NULL,
    name VARCHAR(155) UNIQUE NOT NULL
);

--changeset balex:create-users_roles_table
CREATE TABLE users_roles
(
    user_id   bigserial NOT NULL,
    role_id   bigserial NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- Вставка данных в таблицу 'role'
INSERT INTO role (id, name) VALUES (1, 'USER');
