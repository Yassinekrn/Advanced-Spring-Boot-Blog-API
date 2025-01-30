package com.springboot.blog.springboot_blog_rest_api.services;

import org.springframework.http.ResponseEntity;

import com.springboot.blog.springboot_blog_rest_api.payloads.JwtAuthResponse;
import com.springboot.blog.springboot_blog_rest_api.payloads.LoginDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.SignupDto;

public interface AuthService {
    JwtAuthResponse authenticateUser(LoginDto loginDto);

    ResponseEntity<String> registerUser(SignupDto signupDto);
}
