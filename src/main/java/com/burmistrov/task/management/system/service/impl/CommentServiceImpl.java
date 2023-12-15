package com.burmistrov.task.management.system.service.impl;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.OutCommentDto;
import com.burmistrov.task.management.system.entity.Comment;
import com.burmistrov.task.management.system.entity.Task;
import com.burmistrov.task.management.system.entity.User;
import com.burmistrov.task.management.system.exception.ConflictException;
import com.burmistrov.task.management.system.exception.NotFoundException;
import com.burmistrov.task.management.system.mapper.CommentMapper;
import com.burmistrov.task.management.system.repository.CommentRepository;
import com.burmistrov.task.management.system.repository.TaskRepository;
import com.burmistrov.task.management.system.repository.UserRepository;
import com.burmistrov.task.management.system.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    @Override
    public OutCommentDto addNewComment(long taskId, long userId, NewCommentDto newCommentDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        Task task = taskRepository.findById(taskId);
        if (task == null) {
            throw new NotFoundException("Task not found");
        }
        Comment comment = CommentMapper.toComment(newCommentDto, user, task);
        Comment storageComment = commentRepository.save(comment);
        return CommentMapper.toOutCommentDto(storageComment);
    }

    @Override
    public OutCommentDto updateComment(long comId, long userId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findById(comId);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }
        if(comment.getUser().getId() != userId) {
            throw new ConflictException("User Id: " + userId + " is not the author of the comment");
        }
        comment.setComment(newCommentDto.getComment());
        return CommentMapper.toOutCommentDto(comment);    }

    @Override
    public void deleteComment(long comId, long userId) {
        Comment comment = commentRepository.findById(comId);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }
        if(comment.getUser().getId() != userId) {
            throw new ConflictException("User Id: " + userId + " is not the author of the comment");
        }
        commentRepository.deleteById(comId);
    }
}
