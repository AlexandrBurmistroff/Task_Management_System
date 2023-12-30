package com.burmistrov.task.management.system.mapper;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.CommentDto;
import com.burmistrov.task.management.system.entity.Comment;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto, User author, Task task) {
        return Comment.builder()
                .task(task)
                .authorId(author)
                .comment(newCommentDto.getComment())
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .taskId(comment.getTask().getId())
                .comment(comment.getComment())
                .authorName(comment.getAuthorId().getUsername())
                .created(comment.getCreated())
                .build();
    }
}
