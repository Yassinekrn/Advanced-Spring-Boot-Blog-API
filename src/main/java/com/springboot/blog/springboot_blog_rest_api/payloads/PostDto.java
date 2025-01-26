package com.springboot.blog.springboot_blog_rest_api.payloads;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
    private Long id;

    @NotEmpty(message = "Title is required")
    @Size(min = 2, message = "Title must have at least 2 characters")
    private String title;

    @NotEmpty(message = "Description is required")
    @Size(min = 10, message = "Description must have at least 10 characters")
    private String description;

    @NotEmpty(message = "Content is required")
    private String content;
    private Set<CommentDto> comments;
}
