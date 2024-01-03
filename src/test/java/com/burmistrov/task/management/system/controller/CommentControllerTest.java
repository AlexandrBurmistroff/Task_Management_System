package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.constantstest.ConstantsTest;
import com.burmistrov.task.management.system.dto.comment.CommentDto;
import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Roles;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.service.CommentService;
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
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    CommentService commentService;

    CommentDto commentDto;

    NewCommentDto newCommentDto;

    Task task;

    User author;

    User creator;

    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(1L)
                .username(ConstantsTest.USER_NAME_ALEX)
                .email(ConstantsTest.EMAIL_ALEX)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        creator = User.builder()
                .id(1L)
                .username(ConstantsTest.USER_NAME_TOM)
                .email(ConstantsTest.EMAIL_TOM)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .taskId(1L)
                .authorName(author.getUsername())
                .comment(ConstantsTest.COMMENT)
                .created(ConstantsTest.LOCAL_DATE_TIME)
                .build();

        newCommentDto = NewCommentDto.builder()
                .comment(ConstantsTest.COMMENT)
                .build();

        task = Task.builder()
                .id(1L)
                .title(ConstantsTest.TITLE)
                .description(ConstantsTest.DESCRIPTION)
                .status(Status.IN_WAITING)
                .priority(Priority.HIGH)
                .creator(creator)
                .executor(null)
                .build();
    }

    @Test
    void addComment() throws Exception {
        when(commentService.addNewComment(anyLong(), anyLong(), any())).thenReturn(commentDto);

        mockMvc.perform(post("/comments/1/1")
                        .with(user(ConstantsTest.EMAIL_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.comment", is(commentDto.getComment())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

    @Test
    void updateComment() throws Exception {
        CommentDto updatedComment = CommentDto.builder()
                .id(1L)
                .comment(ConstantsTest.COMMENT_UPDATE)
                .authorName(author.getUsername())
                .created(ConstantsTest.LOCAL_DATE_TIME)
                .build();

        NewCommentDto newComment = NewCommentDto.builder()
                .comment(ConstantsTest.NEW_COMMENT_UPDATE)
                .build();
        when(commentService.updateComment(anyLong(), anyLong(), any())).thenReturn(updatedComment);

        mockMvc.perform(patch("/comments/1/1")
                        .with(user(ConstantsTest.USER_NAME_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newComment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment", is(updatedComment.getComment())));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(delete("/comments/1/1")
                        .with(user(ConstantsTest.USER_NAME_ALEX).password(ConstantsTest.PASSWORD).roles(ConstantsTest.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(commentService, times(1)).deleteComment(anyLong(), anyLong());
    }
}