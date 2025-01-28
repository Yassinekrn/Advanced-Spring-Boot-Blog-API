package com.springboot.blog.springboot_blog_rest_api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.springboot_blog_rest_api.payloads.CommentDto;
import com.springboot.blog.springboot_blog_rest_api.services.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "comments", description = "Comment APIs")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Create comment", description = "Create a new comment for a post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,
            @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments by post ID", description = "Get all comments for a post by post ID")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Get comment by ID", description = "Get a comment by post ID and comment ID")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        return new ResponseEntity<>(commentService.getCommentById(postId, commentId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Update comment", description = "Update a comment by post ID and comment ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDto commentDto) {
        return new ResponseEntity<>(commentService.updateComment(postId, commentId, commentDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Operation(summary = "Delete comment", description = "Delete a comment by post ID and comment ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment entity deleted successfully.", HttpStatus.OK);
    }
}
