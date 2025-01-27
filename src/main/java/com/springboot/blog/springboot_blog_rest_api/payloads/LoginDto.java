package com.springboot.blog.springboot_blog_rest_api.payloads;

import lombok.Data;

@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}
