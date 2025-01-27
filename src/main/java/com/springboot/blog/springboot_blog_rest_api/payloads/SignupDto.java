package com.springboot.blog.springboot_blog_rest_api.payloads;

import lombok.Data;

@Data
public class SignupDto {
    private String name;
    private String username;
    private String email;
    private String password;

}
