package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.CommentDto;
import org.springframework.http.ResponseEntity;

public interface CommentService {

    CommentDto addNewComment(long taskId, long userId, NewCommentDto newCommentDto);

    CommentDto updateComment(long comId, long userId, NewCommentDto newCommentDto);

    void deleteComment(long comId, long userId);
}
