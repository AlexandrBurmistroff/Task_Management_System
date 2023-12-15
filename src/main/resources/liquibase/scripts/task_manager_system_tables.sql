-- liquibase formatted sql

--changeset balex:create-user_entity_table
CREATE TABLE users
(
    id           begint PRIMARY KEY AUTO_INCREMENT NOT NULL,
    username     varchar(155) NOT NULL,
    email        varchar(155) UNIQUE NOT NULL,
    password     varchar NOT NULL
);

--changeset balex:create-task_entity_table
CREATE TABLE task
(
    id          begint PRIMARY KEY AUTO_INCREMENT NOT NULL,
    title       VARCHAR(155) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    status      VARCHAR(50) NOT NULL,
    priority    VARCHAR(50) NOT NULL,
    creator_id  begint NOT NULL,
    executor_id begint,
    CONSTRAINT creator_id_fk FOREIGN KEY (creator_id) REFERENCES users(id),
    CONSTRAINT executor_id_fk FOREIGN KEY (executor_id) REFERENCES users(id)
);

--changeset balex:create-comment_entity_table
CREATE TABLE comments
(
    id      begint PRIMARY KEY AUTO_INCREMENT NOT NULL,
    comment VARCHAR(500) NOT NULL,
    task_id begint NOT NULL,
    user_id begint NOT NULL,
    created timestamp NOT NULL,
    CONSTRAINT task_id_fk FOREIGN KEY (task_id) REFERENCES task(id),
    CONSTRAINT users_id_fk FOREIGN KEY (user_id) REFERENCES users(id)
);

--changeset balex:create-roles_entity_table
CREATE TABLE roles
(
    id   begint PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(155) UNIQUE NOT NULL
);

--changeset balex:create-users_roles_table
CREATE TABLE users_roles
(
    user_id   begint PRIMARY KEY NOT NULL,
    role_id   begint PRIMARY KEY NOT NULL
);

-- Добавление внешнего ключа 'creator_id_fk' к таблице 'task'
ALTER TABLE task ADD CONSTRAINT creator_id_fk FOREIGN KEY (creator_id) REFERENCES users(id);

-- Добавление внешнего ключа 'executor_id_fk' к таблице 'task'
ALTER TABLE task ADD CONSTRAINT executor_id_fk FOREIGN KEY (executor_id) REFERENCES users(id);

-- Добавление внешнего ключа 'task_id_fk' к таблице 'comment'
ALTER TABLE comments ADD CONSTRAINT task_id_fk FOREIGN KEY (task_id) REFERENCES task(id);

-- Добавление внешнего ключа 'author_id_fk' к таблице 'comment'
ALTER TABLE comments ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(id);

-- Добавление внешнего ключа 'user_id_fk' к таблице 'users_roles'
ALTER TABLE users_roles ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES users(id);

-- Добавление внешнего ключа 'role_id_fk' к таблице 'users_roles'
ALTER TABLE users_roles ADD CONSTRAINT role_id_fk FOREIGN KEY (role_id) REFERENCES roles(id);

-- Вставка данных в таблицу 'roles'
INSERT INTO roles (id, name) VALUES (1, 'USER');
