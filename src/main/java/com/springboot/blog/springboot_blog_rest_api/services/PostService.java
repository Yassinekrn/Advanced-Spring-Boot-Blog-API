package com.springboot.blog.springboot_blog_rest_api.services;

import java.util.List;

import com.springboot.blog.springboot_blog_rest_api.payloads.PostDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto, String token);

    PostResponse getAllPosts(int page, int size, String sortBy, String sortOrder);

    PostDto getPostById(Long id);

    PostDto updatePost(Long id, PostDto postDto, String token);

    void deletePost(Long id, String token);

    List<PostDto> searchPosts(String query);
}
