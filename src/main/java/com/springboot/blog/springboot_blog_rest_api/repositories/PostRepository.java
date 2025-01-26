package com.springboot.blog.springboot_blog_rest_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.blog.springboot_blog_rest_api.models.Post;

// no need to add @Repository annotation since JpaRepository is already annotated with @Repository
// JpaRepository is an interface that provides methods for performing CRUD operations on the entity
public interface PostRepository extends JpaRepository<Post, Long> {

}
