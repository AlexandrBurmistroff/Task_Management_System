# Task management system

## Application Description:

The system provides creation, editing, deletion and viewing of tasks.<br>
Each task contains a title, description, status ("pending", "in progress", "completed")<br>
and priority (“high”, “medium”, “low”), as well as the author of the task and the executor.<br><br>

1. The application supports user authentication and authorization using email and password.
2. Access to the API is authenticated using a JWT token.
3. Users can manage their tasks: create new ones, edit existing ones,<br>
   view and delete, change status and assign tasks to performers.
4. Users can view tasks of other users, and task executors can<br>
   change the status of your tasks.
5. You can leave comments on tasks.
6. The API allows you to receive tasks of a specific author or performer, as well as all comments to them.<br>
   Output filtering and pagination is provided.
7. The application handles basic errors and validates incoming data.
8. The application is documented using Open API, Swagger, Java-doc. Swagger UI configured.
9. The code is covered with JUnit tests.

## Application Launch Guide:

Option 1:
1. Clone this application to your PC.
2. Open in Intellij Idea or other unusual development environment.
3. Launch the application.

Option 2:
1. Run via Docker using the command: ```docker-compose up```
2. Open browser, follow the path: ```http://localhost:8080/swagger-ui/index.html#/```

## Examples of requests and responses:

### ```POST: /register```

New User Registration

Request:

```json
{
  "username": "user",
  "email": "user@gmail.com",
  "password": "password"
}
```

Response:

```json
{
  "id": "1",
  "username": "user",
  "email": "user@gmail.com"
}
```

### ```POST: /login```

User authentication

Request:

```json
{
  "email": "user@gmail.com",
  "password": "password"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMkBnbWFpbC5jb20iLCJyb2xlcyI6WyJVU0VSIl0sImlhdCI6MTcwMzY5MjE2NiwiZXhwIjoxNzAzNjkyNzY2fQ.TS15YnlljDuSn71vzo8xlhtYYYk6MDygGNtMj-Ncmbk"
}
```

### ```GET: /task/{taskId}```

Retrieving a task by ID

Request:

```json
{
  "id": 1
}
```

Response:

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

Create a new task

Request:

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

Response:

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

Updating the author's task

Request:

```json
{
  "title": "string",
  "description": "string",
  "status": "IN_WAITING",
  "priority": "LOW",
  "executor_id": 2
}
```

Response:

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

Updating an Executor's Task

Request:

```json
{
  "status": "COMPLETED"
}
```

Response:

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

Deleting a task by an executor

Request:

```json
{
}
```

Response: 204 status

```json
{
}
```

### ```GET: /task```

Getting all tasks

Request:

```json
{
}
```

Response:

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

Retrieving a list of tasks created by a specific user, with additional sorting and pagination

Request:

```json
{
}
```

Response:

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

Retrieving a list of tasks assigned to a specific user, with additional sorting and pagination

Request:

```json
{
}
```

Response:

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

Adding a comment

Request:

```json
{
  "comment": "Comment"
}
```

Response:

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

Comment update

Request:

```json
{
  "comment": "New Comment"
}
```

Response:

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

Deleting a comment

Request:

```json
{
}
```

Response: 204 status

```json
{
}
```

## Technology stack:
- Java 17
- Spring Boot 3.2.0
- Spring Security
- PostgreSQL
- Liquibase
- Docker
- JUnit
