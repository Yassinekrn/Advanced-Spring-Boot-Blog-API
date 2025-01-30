package com.springboot.blog.springboot_blog_rest_api.controllers;

import com.springboot.blog.springboot_blog_rest_api.payloads.JwtAuthResponse;
import com.springboot.blog.springboot_blog_rest_api.payloads.LoginDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.SignupDto;
import com.springboot.blog.springboot_blog_rest_api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth", description = "Authentication APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticate user with username or email and password")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        JwtAuthResponse response = authService.authenticateUser(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register user", description = "Register user with name, username, email and password")
    public ResponseEntity<String> registerUser(@RequestBody SignupDto signupDto) {
        return authService.registerUser(signupDto);
    }
}