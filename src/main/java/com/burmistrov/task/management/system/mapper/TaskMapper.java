package com.burmistrov.task.management.system.mapper;

import com.burmistrov.task.management.system.dto.comment.OutCommentDto;
import com.burmistrov.task.management.system.dto.task.FullTaskDto;
import com.burmistrov.task.management.system.dto.task.NewTaskDto;
import com.burmistrov.task.management.system.dto.task.TaskDto;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Status;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TaskMapper {

    public Task toTask(NewTaskDto newTaskDto, User creator, Status status, Priority priority, User executor) {
        return Task.builder()
                .creator_id(creator)
                .title(newTaskDto.getTitle())
                .description(newTaskDto.getDescription())
                .status(status)
                .priority(priority)
                .executor_id(executor)
                .build();
    }

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(task.getExecutor_id() != null ?
                        UserMapper.toShortUserDto(task.getExecutor_id()) : null)
                .build();
    }

    public FullTaskDto toFullTaskDto(Task task, List<OutCommentDto> comments) {
        return FullTaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .executor(task.getExecutor_id() != null ?
                        UserMapper.toShortUserDto(task.getExecutor_id()) : null)
                .comments(comments)
                .build();
    }
}
