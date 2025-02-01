package com.springboot.blog.springboot_blog_rest_api.services;

import java.util.List;

import com.springboot.blog.springboot_blog_rest_api.payloads.CommentDto;

public interface CommentService {
    CommentDto createComment(Long postId, CommentDto commentDto, String token);

    List<CommentDto> getCommentsByPostId(Long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String token);

    void deleteComment(Long postId, Long commentId, String token);
}
