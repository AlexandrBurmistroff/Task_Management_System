package com.burmistrov.task.management.system.service;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.OutCommentDto;

public interface CommentService {

    OutCommentDto addNewComment(long taskId, long userId, NewCommentDto newCommentDto);

    OutCommentDto updateComment(long comId, long userId, NewCommentDto newCommentDto);

    void deleteComment(long comId, long userId);
}
