package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.task.*;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Sort;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.exception.BadRequestException;
import com.burmistrov.task.management.system.exception.NotFoundCustomException;
import com.burmistrov.task.management.system.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "GET TASK BY ID", description = "Get detailed information about a task by its ID")
    @GetMapping("/{taskId}")
    public ResponseEntity<FullTaskDto> getTaskById(@Parameter(description = "ID of the task")
                                                   @PathVariable long taskId) {
        try {
            if (taskService.getTaskById(taskId) == null) {
                log.info("Task Id: " + taskId + "not found");
            }
        } catch (NotFoundCustomException e) {
            return ResponseEntity.notFound().build();
        }
        log.info("Request to receive a task Id: " + taskId);
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @Operation(summary = "GET TASKS", description = "Get a list of tasks with optional pagination")
    @GetMapping
    public ResponseEntity<List<FullTaskDto>> getTasks(@Parameter(description = "Starting index for pagination, default is 0")
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,

                                                      @Parameter(description = "Number of tasks to retrieve, default is 10")
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        return ResponseEntity.ok(taskService.getTasks(from, size));
    }

    @Operation(summary = "GET TASKS BY CREATOR",
            description = "Get a list of tasks created by a specific user with optional sorting and pagination")
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<FullTaskDto>> getCreatorTasks(@Parameter(description = "ID of the creator")
                                                             @PathVariable long creatorId,

                                                             @Parameter(description = "Sort option, default is STATUS")
                                                             @RequestParam(defaultValue = "STATUS") String sort,

                                                             @Parameter(description = "Starting index for pagination, default is 0")
                                                             @RequestParam(defaultValue = "0") @PositiveOrZero int from,

                                                             @Parameter(description = "Number of tasks to retrieve, default is 10")
                                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Unsupported sort option: " + sort));
        return ResponseEntity.ok(taskService.getTasksByCreatorId(creatorId, enumSort, from, size));
    }

    @Operation(summary = "GET TASKS BY EXECUTOR",
            description = "Get a list of tasks assigned to a specific user with optional sorting and pagination")
    @GetMapping("/executor/{executorId}")
    public ResponseEntity<List<FullTaskDto>> getExecutorTasks(@Parameter(description = "ID of the executor")
                                                              @PathVariable long executorId,

                                                              @Parameter(description = "Sort option, default is STATUS")
                                                              @RequestParam(defaultValue = "STATUS") String sort,

                                                              @Parameter(description = "Starting index for pagination, default is 0")
                                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,

                                                              @Parameter(description = "Number of tasks to retrieve, default is 10")
                                                              @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Request to withdraw tasks");
        Sort enumSort = Sort.from(sort)
                .orElseThrow(() -> new BadRequestException("Unsupported sort option: " + sort));
        return ResponseEntity.ok(taskService.getTasksByExecutorId(executorId, enumSort, from, size));
    }

    @Operation(summary = "CREATE A NEW TASK", description = "Create a new task with the provided details")
    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@Parameter(description = "Details of the new task")
                                              @Valid @RequestBody NewTaskDto newTaskDto) {
        Priority priority = Priority.from(newTaskDto.getPriority())
                .orElseThrow(() -> new BadRequestException("Unknown task priority: " + newTaskDto.getPriority()));
        log.info("Request to add a new task");
        return ResponseEntity.ok(taskService.createTask(newTaskDto, priority));
    }

    @Operation(summary = "UPDATE A TASK BY CREATOR", description = "Update a task with the provided details by its creator")
    @PatchMapping("/{creatorId}/update/{taskId}")
    public ResponseEntity<TaskDto> updateTaskByCreator(@Parameter(description = "ID of the creator")
                                                       @PathVariable long creatorId,

                                                       @Parameter(description = "ID of the task to update")
                                                       @PathVariable long taskId,

                                                       @Parameter(description = "Details to update the task")
                                                       @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("Request to update a task Id: " + taskId);
        return ResponseEntity.ok(taskService.updateTaskByCreator(creatorId, taskId, updateTaskDto));
    }

    @Operation(summary = "UPDATE TASK STATUS BY EXECUTOR", description = "Update the status of a task by its executor")
    @PatchMapping("/update/{executorId}/{taskId}")
    public ResponseEntity<TaskDto> updateTaskStatusByExecutor(@Parameter(description = "ID of the executor")
                                                              @PathVariable long executorId,

                                                              @Parameter(description = "ID of the task to update")
                                                              @PathVariable long taskId,

                                                              @Parameter(description = "Details to update the task status")
                                                              @RequestBody UpdateTaskStatusDto updateTaskStatusDto) {
        log.info("Request to update task status Id: " + taskId + " , executor Id: " + executorId);
        Status status = Status.from(updateTaskStatusDto.getStatus())
                .orElseThrow(() -> new BadRequestException("Unknown task status: " + updateTaskStatusDto.getStatus()));
        return ResponseEntity.ok(taskService.updateTaskStatusByExecutor(executorId, taskId, status));
    }

    @Operation(summary = "DELETE TASK BY CREATOR", description = "Delete a task by its creator")
    @DeleteMapping("/{taskId}/{creatorId}")
    public ResponseEntity<Void> deleteTaskByCreator(@Parameter(description = "ID of the task to delete")
                                                    @PathVariable long taskId,

                                                    @Parameter(description = "ID of the creator")
                                                    @PathVariable long creatorId) {
        log.info("Request to delete a task Id: " + taskId);
        taskService.deleteTaskById(taskId, creatorId);
        return ResponseEntity.noContent().build();
    }
}
