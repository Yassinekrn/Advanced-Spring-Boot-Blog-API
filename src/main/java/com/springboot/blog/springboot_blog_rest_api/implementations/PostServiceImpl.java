package com.springboot.blog.springboot_blog_rest_api.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.springboot.blog.springboot_blog_rest_api.entities.Post;
import com.springboot.blog.springboot_blog_rest_api.entities.User;
import com.springboot.blog.springboot_blog_rest_api.exceptions.ResourceNotFoundException;
import com.springboot.blog.springboot_blog_rest_api.payloads.PostDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.PostResponse;
import com.springboot.blog.springboot_blog_rest_api.repositories.PostRepository;
import com.springboot.blog.springboot_blog_rest_api.repositories.UserRepository;
import com.springboot.blog.springboot_blog_rest_api.security.JwtTokenProvider;
import com.springboot.blog.springboot_blog_rest_api.services.OllamaService;
import com.springboot.blog.springboot_blog_rest_api.services.PostService;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final OllamaService ollamaService;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository, OllamaService ollamaService) {
        this.postRepository = postRepository;
        this.mapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.ollamaService = ollamaService;
    }

    private User getAuthenticatedUser(String token) {
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public PostDto createPost(PostDto postDto, String token) {
        User user = getAuthenticatedUser(token);
        Post post = MapToEntity(postDto);
        post.setOwner(user);
        PostDto postResponse = new PostDto();
        postResponse = MapToDTO(postRepository.save(post));
        return postResponse;

    }

    @Override
    public PostResponse getAllPosts(int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(this::MapToDTO).toList();
        PostResponse postResponse = new PostResponse(
                content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isLast());

        return postResponse;

    }

    private PostDto MapToDTO(Post post) {
        PostDto postDto = mapper.map(post, PostDto.class);
        return postDto;
    }

    private Post MapToEntity(PostDto postDto) {
        Post post = mapper.map(postDto, Post.class);
        return post;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return MapToDTO(post);
    }

    @Override
    public PostDto updatePost(Long id, PostDto postDto, String token) {
        User user = getAuthenticatedUser(token);
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        // Check if the user owns the post
        if (!post.getOwner().getId().equals(user.getId())
                && !user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You can only update your own posts!");
        }
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        return MapToDTO(postRepository.save(post));
    }

    @Override
    public void deletePost(Long id, String token) {
        User user = getAuthenticatedUser(token);
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        if (!post.getOwner().getId().equals(user.getId())
                && !user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You can only delete your own posts!");
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> searchPosts(String query) {
        List<Post> posts = postRepository.searchPosts(query);
        return posts.stream().map(this::MapToDTO).toList();
    }

    @Override
    public String summarizePostContent(Long id, String token) {
        User user = getAuthenticatedUser(token);
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return ollamaService.summarizeText(post.getContent());
    }

}
