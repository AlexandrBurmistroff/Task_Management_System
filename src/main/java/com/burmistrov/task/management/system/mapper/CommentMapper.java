package com.burmistrov.task.management.system.mapper;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.OutCommentDto;
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
                .user(author)
                .comment(newCommentDto.getComment())
                .created(LocalDateTime.now())
                .build();
    }

    public OutCommentDto toOutCommentDto(Comment comment) {
        return OutCommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .username(comment.getUser().getUsername())
                .created(comment.getCreated())
                .build();
    }
}
