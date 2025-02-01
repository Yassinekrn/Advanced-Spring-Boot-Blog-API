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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.springboot_blog_rest_api.payloads.PostDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.PostResponse;
import com.springboot.blog.springboot_blog_rest_api.security.JwtAuthenticationFilter;
import com.springboot.blog.springboot_blog_rest_api.services.PostService;
import com.springboot.blog.springboot_blog_rest_api.utils.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "posts", description = "Post APIs")
public class PostController {
    private PostService postService;

    public PostController(PostService postService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.postService = postService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping
    @Operation(summary = "Create post", description = "Create a new post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return new ResponseEntity<>(postService.createPost(postDto, jwt), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/summarize/{id}")
    @Operation(summary = "Summarize post content", description = "Summarize post content by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> summarizePostContent(@PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return new ResponseEntity<>(postService.summarizePostContent(id, jwt), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get all posts", description = "Get all posts with pagination and sorting")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(required = false, defaultValue = AppConstants.DEFAULT_SORT_ORDER) String sortOrder) {
        return new ResponseEntity<>(postService.getAllPosts(page, size, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Get a post by ID")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PutMapping("/{id}")
    @Operation(summary = "Update post", description = "Update a post by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PostDto> updatePost(@Valid @PathVariable Long id, @RequestBody PostDto postDto,
            @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return new ResponseEntity<>(postService.updatePost(id, postDto, jwt), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post", description = "Delete a post by ID")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deletePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        postService.deletePost(id, jwt);
        return new ResponseEntity<>("Post entity deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPosts(@RequestParam String query) {
        return new ResponseEntity<>(postService.searchPosts(query), HttpStatus.OK);
    }
}
