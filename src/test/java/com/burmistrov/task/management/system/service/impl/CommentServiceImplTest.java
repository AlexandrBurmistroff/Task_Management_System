package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.constantstest.ConstantsTest;
import com.burmistrov.task.management.system.dto.comment.CommentDto;
import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.entity.Comment;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.enums.Priority;
import com.burmistrov.task.management.system.enums.Roles;
import com.burmistrov.task.management.system.enums.Status;
import com.burmistrov.task.management.system.exception.ConflictException;
import com.burmistrov.task.management.system.exception.NotFoundCustomException;
import com.burmistrov.task.management.system.repository.CommentRepository;
import com.burmistrov.task.management.system.repository.TaskRepository;
import com.burmistrov.task.management.system.repository.UserRepository;
import com.burmistrov.task.management.system.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommentServiceImplTest {


    @MockBean
    CommentRepository commentRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    TaskRepository taskRepository;

    @Autowired
    CommentService commentService;

    Task task;

    User author;

    User creator;

    Comment comment;

    CommentDto commentDto;

    NewCommentDto newCommentDto;

    CommentDto newComment;


    @BeforeEach
    void setUp() {
        author = User.builder()
                .id(1L)
                .username(ConstantsTest.USER_NAME_ALEX)
                .email(ConstantsTest.EMAIL_ALEX)
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

        comment = Comment.builder()
                .task(task)
                .authorId(author)
                .comment(ConstantsTest.COMMENT)
                .created(ConstantsTest.LOCAL_DATE_TIME)
                .build();

        newComment = CommentDto.builder()
                .id(comment.getId())
                .comment(ConstantsTest.COMMENT)
                .authorName(comment.getAuthorId().getUsername())
                .created(comment.getCreated())
                .build();

        creator = User.builder()
                .id(1L)
                .username(ConstantsTest.USER_NAME_TOM)
                .email(ConstantsTest.EMAIL_TOM)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();
    }

    @Test
    void addNewCommentToTask() {
        when(userRepository.findUserById(2L)).thenReturn(author);
        when(taskRepository.findById(1L)).thenReturn(task);
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto actualComment = commentService.addNewComment(1L, 2L, newCommentDto);

        Assertions.assertEquals(comment.getId(), actualComment.getId());
        Assertions.assertEquals(comment.getComment(), actualComment.getComment());
        Assertions.assertEquals(comment.getAuthorId().getUsername(), actualComment.getAuthorName());
        Assertions.assertEquals(comment.getCreated(), actualComment.getCreated());
    }

    @Test
    void addValidCommentWithUserNotFound() {
        author = User.builder()
                .id(3L)
                .username(ConstantsTest.USER_NAME_SARA)
                .email(ConstantsTest.EMAIL_SARA)
                .password(ConstantsTest.PASSWORD)
                .roles(new HashSet<>(Roles.USER.ordinal()))
                .build();

        NotFoundCustomException exception = Assertions.assertThrows(NotFoundCustomException.class, () ->
                commentService.addNewComment(1L, author.getId(), newCommentDto));
        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void addValidCommentWithTaskNotFound() {
        when(userRepository.findUserById(1L)).thenReturn(author);

        NotFoundCustomException exception = Assertions.assertThrows(NotFoundCustomException.class, () ->
                commentService.addNewComment(3L, author.getId(), newCommentDto));
        Assertions.assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void updateCommentWithCommentNotFound() {
        NotFoundCustomException exception = Assertions.assertThrows(NotFoundCustomException.class, () ->
                commentService.updateComment(3L, author.getId(), newCommentDto));
        Assertions.assertEquals("Comment not found", exception.getMessage());
    }

    @Test
    void updateCommentWithUserNotAuthor() {
        when(commentRepository.findById(anyLong())).thenReturn(comment);

        ConflictException exception = Assertions.assertThrows(ConflictException.class, () ->
                commentService.updateComment(1L, 3L, newCommentDto));
        Assertions.assertEquals("User Id: " + 3L + " is not the author of the comment", exception.getMessage());
    }

    @Test
    void updateCommentByAuthor() {
        when(commentRepository.findById(anyLong())).thenReturn(comment);

        CommentDto actualComment = commentService.updateComment(1L, 1L,
                new NewCommentDto(ConstantsTest.COMMENT));
        Assertions.assertEquals(actualComment.getComment(), newComment.getComment());
    }

    @Test
    void deleteCommentByIdTest() {
        when(commentRepository.findById(1L)).thenReturn(comment);

        doNothing().when(commentRepository).deleteById(1L);
        commentService.deleteComment(1L, 1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFoundCommentTest() {
        NotFoundCustomException exception = Assertions.assertThrows(NotFoundCustomException.class, () ->
                commentService.updateComment(3L, author.getId(), newCommentDto));
        Assertions.assertEquals("Comment not found", exception.getMessage());
    }
}