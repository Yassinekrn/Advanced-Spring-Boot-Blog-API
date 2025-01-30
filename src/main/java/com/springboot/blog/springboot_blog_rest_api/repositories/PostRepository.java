package com.springboot.blog.springboot_blog_rest_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.blog.springboot_blog_rest_api.entities.Post;

// no need to add @Repository annotation since JpaRepository is already annotated with @Repository
// JpaRepository is an interface that provides methods for performing CRUD operations on the entity
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.title LIKE %?1% OR p.description LIKE %?1%")
    List<Post> searchPosts(String query);

}
