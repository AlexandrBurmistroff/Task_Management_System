package com.burmistrov.task.management.system.controller;

import com.burmistrov.task.management.system.dto.comment.NewCommentDto;
import com.burmistrov.task.management.system.dto.comment.CommentDto;
import com.burmistrov.task.management.system.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for adding, updating, deleting an ad
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tasks/comments")

public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "ADD COMMENT", description = "Endpoint to add comment")
    @ApiResponse(responseCode = "200", description = "Added commit",
            content = @Content(schema = @Schema(implementation = CommentDto.class)))
    @PostMapping("/{taskId}/{userId}")
    public ResponseEntity<CommentDto> addComment(@Parameter(description = "Task ID")
                                                 @PathVariable long taskId,

                                                 @Parameter(description = "User ID")
                                                 @PathVariable long userId,

                                                 @Parameter(description = "Add new comment")
                                                 @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Request to add a comment");
        return ResponseEntity.ok(commentService.addNewComment(taskId, userId, newCommentDto));
    }

    @Operation(summary = "UPDATE COMMENT", description = "Endpoint to update comment")
    @ApiResponse(responseCode = "200", description = "Update commit",
            content = @Content(schema = @Schema(implementation = CommentDto.class)))
    @PatchMapping("/{commentId}/{userId}")
    public ResponseEntity<CommentDto> updateComment(@Parameter(description = "Comment ID")
                                                    @PathVariable long commentId,

                                                    @Parameter(description = "User ID")
                                                    @PathVariable long userId,

                                                    @Parameter(description = "Updated comment details")
                                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("Request to change a comment");
        return ResponseEntity.ok(commentService.updateComment(commentId, userId, newCommentDto));
    }

    @Operation(summary = "DELETE COMMENT", description = "Endpoint to delete comment")
    @ApiResponse(responseCode = "204", description = "Delete commit",
            content = @Content(schema = @Schema(implementation = CommentDto.class)))
    @DeleteMapping("/{commentId}/{userId}")
    public ResponseEntity <Void> deleteComment(@Parameter(description = "Comment ID")
                              @PathVariable long commentId,

                              @Parameter(description = "User ID")
                              @PathVariable long userId) {
        log.info("Request from a user to delete their comment");
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
