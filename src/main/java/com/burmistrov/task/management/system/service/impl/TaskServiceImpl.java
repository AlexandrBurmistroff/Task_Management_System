package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.dto.comment.OutCommentDto;
import com.burmistrov.task.management.system.dto.task.FullTaskDto;
import com.burmistrov.task.management.system.dto.task.NewTaskDto;
import com.burmistrov.task.management.system.dto.task.TaskDto;
import com.burmistrov.task.management.system.dto.task.UpdateTaskDto;
import com.burmistrov.task.management.system.entity.Comment;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Sort;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.exception.BadRequestException;
import com.burmistrov.task.management.system.exception.ConflictException;
import com.burmistrov.task.management.system.exception.NotFoundException;
import com.burmistrov.task.management.system.mapper.CommentMapper;
import com.burmistrov.task.management.system.mapper.TaskMapper;
import com.burmistrov.task.management.system.repository.CommentRepository;
import com.burmistrov.task.management.system.repository.TaskRepository;
import com.burmistrov.task.management.system.repository.UserRepository;
import com.burmistrov.task.management.system.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public TaskDto createTask(NewTaskDto newTaskDto, Priority priority) {
        User creator = userRepository.findUserById(newTaskDto.getCreator_id());
        if (creator == null) {
            throw new NotFoundException("User with email address: " + newTaskDto.getCreator_id() + " already exists");
        }
        User executor;
        if (newTaskDto.getExecutor_id() != null) {
            executor = userRepository.findUserById(newTaskDto.getExecutor_id());
            if (executor == null) {
                throw new NotFoundException("User with email address: " + newTaskDto.getExecutor_id() + " already exists");
            }
        } else {
            executor = null;
        }
        Task task = TaskMapper.toTask(newTaskDto, creator, Status.IN_WAITING, priority, executor);
        Task taskStorage = taskRepository.save(task);
        return TaskMapper.toTaskDto(taskStorage);
    }

    @Override
    public TaskDto updateTaskByCreator(long creatorId, long taskId, UpdateTaskDto updateTaskDto) {
        checkUser(creatorId);
        Task task = checkTask(taskId);
        if (task.getCreator_id().getId() != creatorId) {
            throw new ConflictException("The user is not the task creator");
        }
        if (updateTaskDto.getTitle() != null) {
            task.setTitle(updateTaskDto.getTitle());
        }
        if (updateTaskDto.getDescription() != null) {
            task.setDescription(updateTaskDto.getDescription());
        }
        if (updateTaskDto.getStatus() != null) {
            Status status = Status.from(updateTaskDto.getStatus())
                    .orElseThrow(() -> new BadRequestException("Unknown task status: " + updateTaskDto.getStatus()));
            task.setStatus(status);
        }
        if (updateTaskDto.getPriority() != null) {
            Priority priority = Priority.from(updateTaskDto.getPriority())
                    .orElseThrow(() -> new BadRequestException("Unknown task priority: " + updateTaskDto.getPriority()));
            task.setPriority(priority);
        }
        if (updateTaskDto.getExecutor_id() != null) {
            User executor = userRepository.findUserById(updateTaskDto.getExecutor_id());
            if (executor == null) {
                throw new NotFoundException("User Id: " + updateTaskDto.getExecutor_id() + " not found");
            }
            task.setExecutor_id(executor);
        }
        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toTaskDto(updatedTask);
    }

    @Override
    public TaskDto updateTaskStatusByExecutor(long executorId, long taskId, Status status) {
        checkUser(executorId);
        Task task = checkTask(taskId);
        if (executorId != task.getExecutor_id().getId()) {
            throw new ConflictException("The user is not the task creator");
        }
        task.setStatus(status);
        Task updateTaskStatus = taskRepository.save(task);
        return TaskMapper.toTaskDto(updateTaskStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public FullTaskDto getTaskById(long id) {
        Task task = checkTask(id);
        List<Comment> taskComments = commentRepository.findCommentsByTaskId(id);
        List<OutCommentDto> commentDtos = taskComments.stream()
                .map(CommentMapper::toOutCommentDto)
                .collect(Collectors.toList());
        return TaskMapper.toFullTaskDto(task, commentDtos);
    }

    @Override
    public void deleteTaskById(long taskId, long creatorId) {
        Task task = checkTask(taskId);
        checkUser(creatorId);
        if (task.getCreator_id().getId() != creatorId) {
            throw new ConflictException("The user is not the task creator");
        }
        taskRepository.deleteById(taskId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasks(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Task> tasks = taskRepository.findAll(pageable);
        List<Task> taskList = tasks.getContent();
        return getFullTaskDtos(taskList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasksByCreatorId(long creatorId, Sort sort, int from, int size) {
        checkUser(creatorId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Task> tasks = switch (sort) {
            case STATUS -> taskRepository.findTasksByCreatorIdOrderByStatusDesc(creatorId, pageable);
            case PRIORITY -> taskRepository.findTasksByCreatorIdOrderByPriorityDesc(creatorId, pageable);
        };
        return getFullTaskDtos(tasks);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasksByExecutorId(long executorId, Sort sort, int from, int size) {
        checkUser(executorId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Task> tasks = switch (sort) {
            case STATUS -> taskRepository.findTasksByExecutorIdOrderByStatusDesc(executorId, pageable);
            case PRIORITY -> taskRepository.findTasksByExecutorIdOrderByPriorityDesc(executorId, pageable);
        };
        return getFullTaskDtos(tasks);
    }

    private List<FullTaskDto> getFullTaskDtos(List<Task> tasks) {
        List<Long> taskIds = tasks.stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        List<OutCommentDto> tasksComments = commentRepository.findCommentsByTaskIdIn(taskIds).stream()
                .map(CommentMapper::toOutCommentDto)
                .collect(Collectors.toList());
        return tasks.stream()
                .map(fullTaskDto -> TaskMapper.toFullTaskDto(fullTaskDto, tasksComments))
                .collect(Collectors.toList());
    }

    private void checkUser(long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("User Id: " + userId + " not found");
        }
    }

    private Task checkTask(long taskId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new NotFoundException("Task Id: " + taskId + " not found");
        }
        return task;
    }
}
