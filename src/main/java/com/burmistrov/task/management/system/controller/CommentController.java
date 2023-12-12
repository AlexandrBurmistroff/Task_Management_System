package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.OutCommentDto;
import com.burmistrov.task.management.system.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{taskId}/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OutCommentDto addComment(@PathVariable long taskId,
                                    @PathVariable long userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Request to add a comment");
        return commentService.addNewComment(taskId, userId, newCommentDto);
    }

    @PatchMapping("/{comId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public OutCommentDto updateComment(@PathVariable long comId,
                                       @PathVariable long userId,
                                       @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Request to change a comment");
        return commentService.updateComment(comId, userId, newCommentDto);
    }

    @DeleteMapping("/{comId}/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long comId, @PathVariable long userId) {
        log.info("Request from a user to delete their comment");
        commentService.deleteComment(comId, userId);
    }
}
