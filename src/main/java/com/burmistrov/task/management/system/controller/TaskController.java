package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.task.*;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Sort;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.exception.BadRequestException;
import com.burmistrov.task.management.system.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

//    @GetMapping("/user/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<TaskDto> getTasks(@PathVariable Long userId) {
//        return new ResponseEntity<>(taskService.getTasks(userId));
//    }
//
//    @GetMapping("/{taskId}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<TaskDto> getTask(@PathVariable Long taskId) {
//        return new ResponseEntity<>(taskService.getTask(taskId));
//    }
//
//    @PostMapping
//    public ResponseEntity<TaskDto> addTask(@RequestBody addTask) {
//
//    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public FullTaskDto getTaskById(@PathVariable long taskId) {
        log.info("Request to receive a task Id: " + taskId);
        return taskService.getTaskById(taskId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FullTaskDto> getTasks(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        return taskService.getTasks(from, size);
    }

    @GetMapping("/creator/{creatorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FullTaskDto> getCreatorTasks(@PathVariable long creatorId,
                                             @RequestParam(defaultValue = "STATUS") String sort,
                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Unsupported sort option: " + sort));
        return taskService.getTasksByCreatorId(creatorId, enumSort, from, size);
    }

    @GetMapping("/executor/{executorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FullTaskDto> getExecutorTasks(@PathVariable long executorId,
                                              @RequestParam(defaultValue = "STATUS") String sort,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Unsupported sort option: " + sort));
        return taskService.getTasksByExecutorId(executorId, enumSort, from, size);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto createTask(@Valid @RequestBody NewTaskDto newTaskDto) {
        Priority priority = Priority.from(newTaskDto.getPriority())
                .orElseThrow(() -> new BadRequestException("Unknown task priority: " + newTaskDto.getPriority()));
        log.info("Request to add a new task");
        return taskService.createTask(newTaskDto, priority);
    }

    @PatchMapping("{creatorId}/update/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTaskByCreator(@PathVariable long creatorId, @PathVariable long taskId,
                                       @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("Request to update a task Id: " + taskId);
        return taskService.updateTaskByCreator(creatorId, taskId, updateTaskDto);
    }

    @PatchMapping("/update/{executorId}/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto updateTaskStatusByExecutor(@PathVariable long executorId, @PathVariable long taskId,
                                              @RequestBody UpdateTaskStatusDto updateTaskStatusDto) {
        log.info("Request to update task status Id: " + taskId + " , executor Id: " + executorId);
        Status status = Status.from(updateTaskStatusDto.getStatus())
                .orElseThrow(() -> new BadRequestException("Unknown task status: " + updateTaskStatusDto.getStatus()));
        return taskService.updateTaskStatusByExecutor(executorId, taskId, status);
    }

    @DeleteMapping("/{taskId}/{creatorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskByCreator(@PathVariable long taskId, @PathVariable long creatorId) {
        log.info("Request to delete a task Id: " + taskId);
        taskService.deleteTaskById(taskId, creatorId);
    }
}
