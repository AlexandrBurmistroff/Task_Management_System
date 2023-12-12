package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.task.FullTaskDto;
import com.burmistrov.task.management.system.dto.task.NewTaskDto;
import com.burmistrov.task.management.system.dto.task.TaskDto;
import com.burmistrov.task.management.system.dto.task.UpdateTaskDto;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Sort;
import com.burmistrov.task.management.system.enums.Status;

import java.util.List;

public interface TaskService {

    TaskDto createTask(NewTaskDto newTaskDto, Priority priority);

    TaskDto updateTaskByCreator(long creatorId, long taskId, UpdateTaskDto updateTaskDto);

    TaskDto updateTaskStatusByExecutor(long executorId, long taskId, Status status);

    FullTaskDto getTaskById(long id);

    void deleteTaskById(long taskId, long creatorId);

    List<FullTaskDto> getTasks(int from, int size);

    List<FullTaskDto> getTasksByCreatorId(long creatorId, Sort sort, int from, int size);

    List<FullTaskDto> getTasksByExecutorId(long executorId, Sort sort, int from, int size);
}
