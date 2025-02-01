package com.springboot.blog.springboot_blog_rest_api.payloads;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotEmpty(message = "Body is required")
    @Size(min = 10, message = "Body must have at least 10 characters")
    private String body;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserDto owner;
}
