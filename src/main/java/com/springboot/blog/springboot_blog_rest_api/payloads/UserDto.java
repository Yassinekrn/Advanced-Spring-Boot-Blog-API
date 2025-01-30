package com.springboot.blog.springboot_blog_rest_api.payloads;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
