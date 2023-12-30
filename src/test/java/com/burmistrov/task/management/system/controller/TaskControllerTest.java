package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.constantstest.ConstantsTest;
import com.burmistrov.task.management.system.dto.task.FullTaskDto;
import com.burmistrov.task.management.system.dto.task.NewTaskDto;
import com.burmistrov.task.management.system.dto.task.TaskDto;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Roles;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.mapper.UserMapper;
import com.burmistrov.task.management.system.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    TaskService taskService;

    NewTaskDto newTaskDto;

    TaskDto taskDto;

    TaskDto upTaskDto;

    FullTaskDto fullTaskDto;

    User creator;

    User executor;

    @BeforeEach
    void setUp() {
        creator = User.builder()
                .id(1L)
                .username(ConstantsTest.USER_NAME_TOM)
                .email(ConstantsTest.EMAIL_TOM)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        executor = User.builder()
                .id(2L)
                .username(ConstantsTest.USER_NAME_ALEX)
                .email(ConstantsTest.EMAIL_ALEX)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        newTaskDto = NewTaskDto.builder()
                .creator_id(1L)
                .title(ConstantsTest.TITLE)
                .description(ConstantsTest.DESCRIPTION)
                .status(ConstantsTest.STATUS_IN_WAITING)
                .priority(ConstantsTest.PRIORITY_HIGH)
                .executor_id(2L)
                .build();

        taskDto = TaskDto.builder()
                .id(1L)
                .creator(UserMapper.toShortUserDto(creator))
                .title(ConstantsTest.TITLE)
                .description(ConstantsTest.DESCRIPTION)
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(UserMapper.toShortUserDto(executor))
                .build();

        upTaskDto = TaskDto.builder()
                .id(1L)
                .creator(UserMapper.toShortUserDto(creator))
                .title(ConstantsTest.TITLE_UPDATE)
                .description(ConstantsTest.DESCRIPTION_UPDATE)
                .status(Status.IN_PROGRESS)
                .priority(Priority.HIGH)
                .executor(UserMapper.toShortUserDto(executor))
                .build();

        fullTaskDto = FullTaskDto.builder()
                .id(1L)
                .creator(UserMapper.toShortUserDto(creator))
                .title(ConstantsTest.TITLE)
                .description(ConstantsTest.DESCRIPTION)
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .executor(UserMapper.toShortUserDto(executor))
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    void getTaskById() throws Exception {
        when(taskService.getTaskById(anyLong())).thenReturn(fullTaskDto);

        mockMvc.perform(get("/tasks/1")
                        .with(user(ConstantsTest.EMAIL_TOM).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(fullTaskDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(fullTaskDto.getTitle())))
                .andExpect(jsonPath("$.description", is(fullTaskDto.getDescription())))
                .andExpect(jsonPath("$.status", is(fullTaskDto.getStatus().toString())))
                .andExpect(jsonPath("$.priority", is(fullTaskDto.getPriority().toString())));
    }

    @Test
    void addTaskTest() throws Exception {
        when(taskService.createTask(any(),any())).thenReturn(taskDto);

        mockMvc.perform(post("/tasks/create")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(taskDto.getTitle())))
                .andExpect(jsonPath("$.description", is(taskDto.getDescription())))
                .andExpect(jsonPath("$.status", is(taskDto.getStatus().toString())))
                .andExpect(jsonPath("$.priority", is(taskDto.getPriority().toString())));
    }

    @Test
    void updateTaskByCreatorTest() throws Exception {
        when(taskService.updateTaskByCreator(anyLong(), anyLong(), any())).thenReturn(upTaskDto);

        mockMvc.perform(patch("/tasks/1/update/1")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(upTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(upTaskDto.getId()), Long.class))
                .andExpect(jsonPath("$.title", is(upTaskDto.getTitle())))
                .andExpect(jsonPath("$.description", is(upTaskDto.getDescription())))
                .andExpect(jsonPath("$.status", is(upTaskDto.getStatus().toString())))
                .andExpect(jsonPath("$.priority", is(upTaskDto.getPriority().toString())));
    }

    @Test
    void updateTaskStatusByExecutorTest() throws Exception {
        taskDto.setStatus(Status.IN_PROGRESS);
        when(taskService.updateTaskStatusByExecutor(anyLong(), anyLong(), any())).thenReturn(taskDto);

        mockMvc.perform(patch("/tasks/update/2/1")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(upTaskDto.getStatus().toString())));
    }

    @Test
    void deleteTaskByIdTest() throws Exception {
        mockMvc.perform(delete("/tasks/1/1")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(taskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTaskById(anyLong(), anyLong());
    }

    @Test
    void getTasksTest() throws Exception {
        when(taskService.getTasks(anyInt(), anyInt())).thenReturn(List.of(fullTaskDto));

        mockMvc.perform(get("/tasks")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fullTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(fullTaskDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].title", is(fullTaskDto.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(fullTaskDto.getDescription())))
                .andExpect(jsonPath("$.[0].status", is(fullTaskDto.getStatus().toString())))
                .andExpect(jsonPath("$.[0].priority", is(fullTaskDto.getPriority().toString())));
    }

    @Test
    void getCreatorTasksTest() throws Exception {
        when(taskService.getTasksByCreatorId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(fullTaskDto));

        mockMvc.perform(get("/tasks/creator/1?status=STATUS&from=0&size=10")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fullTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(fullTaskDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].title", is(fullTaskDto.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(fullTaskDto.getDescription())))
                .andExpect(jsonPath("$.[0].status", is(fullTaskDto.getStatus().toString())))
                .andExpect(jsonPath("$.[0].priority", is(fullTaskDto.getPriority().toString())));
    }

    @Test
    void getExecutorTasksTest() throws Exception {
        when(taskService.getTasksByExecutorId(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(fullTaskDto));

        mockMvc.perform(get("/tasks/executor/2?status=STATUS&from=0&size=10")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(fullTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(fullTaskDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].title", is(fullTaskDto.getTitle())))
                .andExpect(jsonPath("$.[0].description", is(fullTaskDto.getDescription())))
                .andExpect(jsonPath("$.[0].status", is(fullTaskDto.getStatus().toString())))
                .andExpect(jsonPath("$.[0].priority", is(fullTaskDto.getPriority().toString())));
    }
}