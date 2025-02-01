package com.springboot.blog.springboot_blog_rest_api.implementations;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.springboot_blog_rest_api.entities.Comment;
import com.springboot.blog.springboot_blog_rest_api.entities.Post;
import com.springboot.blog.springboot_blog_rest_api.entities.User;
import com.springboot.blog.springboot_blog_rest_api.exceptions.BlogAPIException;
import com.springboot.blog.springboot_blog_rest_api.exceptions.ResourceNotFoundException;
import com.springboot.blog.springboot_blog_rest_api.payloads.CommentDto;
import com.springboot.blog.springboot_blog_rest_api.repositories.CommentRepository;
import com.springboot.blog.springboot_blog_rest_api.repositories.PostRepository;
import com.springboot.blog.springboot_blog_rest_api.repositories.UserRepository;
import com.springboot.blog.springboot_blog_rest_api.security.JwtTokenProvider;
import com.springboot.blog.springboot_blog_rest_api.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
            ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    // TODO: refactor me in utils
    private User getAuthenticatedUser(String token) {
        String username = jwtTokenProvider.getUsernameFromJWT(token);
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto, String token) {
        User user = getAuthenticatedUser(token);
        Comment comment = mapToEntity(commentDto);
        comment.setOwner(user);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    private CommentDto mapToDTO(Comment comment) {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        return comment;
    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDTO).toList();
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }

        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto, String token) {
        User user = getAuthenticatedUser(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }

        if (!comment.getOwner().getId().equals(user.getId())
                && !user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You can only update your own comments!");
        }

        // comment.setName(commentDto.getName());
        // comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId, String token) {
        User user = getAuthenticatedUser(token);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        if (comment.getPost().getId() != post.getId()) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }

        if (!comment.getOwner().getId().equals(user.getId())
                && !user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("You can only delete your own comments!");
        }

        commentRepository.delete(comment);
    }

}
