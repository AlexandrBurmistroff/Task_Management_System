# Система управления задачами

## Описание приложения:

Система обеспечивает создание, редактирование, удаление и просмотр задач.<br>
Каждая задача содержит заголовок, описание, статус("в ожидании", "в процессе", "завершено")<br>
и приоритет("высокий", "средний", "низкий"), а также автора задачи и исполнителя.<br><br>

1. Приложение поддерживает аутентификацию и авторизацию пользователей по email и паролю.
2. Доступ к API аутентифицировано с помощью JWT токена.
3. Пользователи могут управлять своими задачами: создавать новые, редактировать существующие,<br>
просматривать и удалять, менять статус и назначать исполнителей задачи.
4. Пользователи могут просматривать задачи других пользователей, а исполнители задачи могут<br>
менять статус своих задач.
5. К задачам можно оставлять комметнарии.
6. API позволяет получать задачи конкретного автора или исполнителя, а также все комментарии к ним.<br>
Обеспечена фильтрация и пагинация вывода.
7. Приложение обрабатывает основные ошибки, и валидирует входящие данные.
8. Приложение задукоментировано с помощью Open API, Swagger, Java-doc. Настроен Swagger UI.
9. Код покрыт JUnit тестами.

## Руководство по запуску приложения:

Вариант 1:
1. Склонировать данное приложение к себе на ПК.
2. Открыть в Intellij Idea или другой аналогичной среде разработки.
3. Запустить приложение.

Вариант 2:
1. Запустить через Docker с помощью команды: ```docker-compose up```
2. Открыть браузер, пройти по пути: ```http://localhost:8080/swagger-ui/index.html#/``` 

## Примеры запросов и ответов:

### ```POST: /register```

Регистрация нового пользователя

Запрос:

```json
{
  "username": "user",
  "email": "user@gmail.com",
  "password": "password"
}
```

Ответ:

```json
{
  "id": "1",
  "username": "user",
  "email": "user@gmail.com"
}
```

### ```POST: /login```

Аутентификация пользователя

Запрос:

```json
{
  "email": "user@gmail.com",
  "password": "password"
}
```

Ответ:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcwMzY5MjE2NiwiZXhwIjoxNzAzNjkyNzY2fQ.TS15YnlljDuSn71vzo8xlhtYYYk6MDygGNtMj-Ncmbk"
}
```

### ```GET: /task/{taskId}```

Получение задачи по ID

Запрос:

```json
{
  "id": 1
}
```

Ответ:

```json
{
  "id": 1,
  "title": "string",
  "description": "string",
  "status": "IN_WAITING",
  "priority": "LOW",
  "creator": {
    "id": 1,
    "username": "user"
  },
  "executor": {
    "id": 1,
    "username": "user"
  },
  "comments": [
    {
      "id": 1,
      "taskId": 1,
      "authorName": "string",
      "comment": "string",
      "created": "2023-12-27 16:28:28"
    },
    {
      "id": 2,
      "taskId": 1,
      "authorName": "string",
      "comment": "string",
      "created": "2023-12-27 16:49:49"
    }
  ]
}
```

### ```POST: /task/create```

Создание новой задачи

Запрос:

```json
{
  "creator_id": 1,
  "title": "title",
  "description": "title",
  "status": "IN_WAITING",
  "priority": "LOW",
  "executor_id": 1
}
```

Ответ:

```json
{
  "id": 2,
  "title": "title",
  "description": "title",
  "status": "IN_WAITING",
  "priority": "LOW",
  "creator": {
    "id": 2,
    "username": "string"
  },
  "executor": {
    "id": 2,
    "username": "string"
  }
}
```
### ```PATCH: /task/{creatorId}/update/{taskId}```

Обновление задачи автора

Запрос:

```json
{
  "title": "string",
  "description": "string",
  "status": "IN_WAITING",
  "priority": "LOW",
  "executor_id": 2
}
```

Ответ:

```json
{
  "id": 2,
  "title": "string",
  "description": "string",
  "status": "IN_WAITING",
  "priority": "LOW",
  "creator": {
    "id": 2,
    "username": "string"
  },
  "executor": {
    "id": 2,
    "username": "string"
  }
}
```

### ```PATCH: /update/{executorId}/{taskId}```

Обновление задачи исполнителя

Запрос:

```json
{
  "status": "COMPLETED"
}
```

Ответ:

```json
{
  "id": 2,
  "title": "title",
  "description": "title",
  "status": "COMPLETED",
  "priority": "LOW",
  "creator": {
    "id": 2,
    "username": "string"
  },
  "executor": {
    "id": 2,
    "username": "string"
  }
}
```

### ```DELETE: /update/{executorId}/{taskId}```

Удаление задачи исполнителем

Запрос:

```json
{
}
```

Ответ: 204 статус

```json
{
}
```

### ```GET: /task```

Получение всех задач

Запрос:

```json
{
}
```

Ответ:

```json
[
  {
    "id": 1,
    "title": "string",
    "description": "string",
    "status": "IN_WAITING",
    "priority": "LOW",
    "creator": {
      "id": 1,
      "username": "user"
    },
    "executor": {
      "id": 1,
      "username": "user"
    },
    "comments": [
      {
        "id": 1,
        "taskId": 1,
        "authorName": "string",
        "comment": "string",
        "created": "2023-12-27 16:28:28"
      },
      {
        "id": 2,
        "taskId": 1,
        "authorName": "string",
        "comment": "string5",
        "created": "2023-12-27 16:49:49"
      }
    ]
  },
  {
    "id": 3,
    "title": "title",
    "description": "title",
    "status": "IN_WAITING",
    "priority": "LOW",
    "creator": {
      "id": 2,
      "username": "string"
    },
    "executor": {
      "id": 2,
      "username": "string"
    },
    "comments": [
      {
        "id": 1,
        "taskId": 1,
        "authorName": "string",
        "comment": "string",
        "created": "2023-12-27 16:28:28"
      },
      {
        "id": 2,
        "taskId": 1,
        "authorName": "string",
        "comment": "string5",
        "created": "2023-12-27 16:49:49"
      }
    ]
  }
]
```

### ```GET: /tasks/creator/1?sort=STATUS&from=0&size=10```

Получение списка задач, созданных конкретным пользователем, с дополнительной сортировкой и нумерацией страниц

Запрос:

```json
{
}
```

Ответ:

```json
[
  {
    "id": 1,
    "title": "string",
    "description": "string",
    "status": "IN_WAITING",
    "priority": "LOW",
    "creator": {
      "id": 1,
      "username": "user"
    },
    "executor": {
      "id": 1,
      "username": "user"
    },
    "comments": [
      {
        "id": 1,
        "taskId": 1,
        "authorName": "string",
        "comment": "string",
        "created": "2023-12-27 16:28:28"
      },
      {
        "id": 2,
        "taskId": 1,
        "authorName": "string",
        "comment": "string5",
        "created": "2023-12-27 16:49:49"
      }
    ]
  }
]
```

### ```GET: /tasks/executor/2?sort=STATUS&from=0&size=10```

Получение списка задач, назначенных конкретному пользователю, с дополнительной сортировкой и нумерацией страниц

Запрос:

```json
{
}
```

Ответ:

```json
[
  {
    "id": 3,
    "title": "title",
    "description": "title",
    "status": "IN_WAITING",
    "priority": "LOW",
    "creator": {
      "id": 2,
      "username": "string"
    },
    "executor": {
      "id": 2,
      "username": "string"
    },
    "comments": []
  }
]
```

### ```POST: /comments/{taskId}/{userId}```

Добавление комментария

Запрос:

```json
{
  "comment": "Comment"
}
```

Ответ:

```json
{
  "id": 3,
  "taskId": 1,
  "authorName": "string",
  "comment": "Comment",
  "created": "2023-12-30 16:16:20"
}
```

### ```PATCH: /comments/{commentId}/{userId}```

Обновление комментария

Запрос:

```json
{
  "comment": "New Comment"
}
```

Ответ:

```json
{
  "id": 3,
  "taskId": 1,
  "authorName": "string",
  "comment": "New Comment",
  "created": "2023-12-30 16:16:20"
}
```

### ```DELETE: /comments/{commentId}/{userId}```

Удаление комментария

Запрос:

```json
{
}
```

Ответ: 204 статус

```json
{
}
```

## Стек технологий:
- Java 17
- Spring Boot 3.2.0
- Spring Security
- PostgreSQL
- Liquibase
- Docker
- JUnit
