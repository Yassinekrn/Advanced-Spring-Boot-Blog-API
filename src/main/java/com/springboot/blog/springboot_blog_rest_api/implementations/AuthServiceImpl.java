package com.springboot.blog.springboot_blog_rest_api.implementations;

import com.springboot.blog.springboot_blog_rest_api.entities.Role;
import com.springboot.blog.springboot_blog_rest_api.entities.User;
import com.springboot.blog.springboot_blog_rest_api.payloads.JwtAuthResponse;
import com.springboot.blog.springboot_blog_rest_api.payloads.LoginDto;
import com.springboot.blog.springboot_blog_rest_api.payloads.SignupDto;
import com.springboot.blog.springboot_blog_rest_api.repositories.RoleRepository;
import com.springboot.blog.springboot_blog_rest_api.repositories.UserRepository;
import com.springboot.blog.springboot_blog_rest_api.security.JwtTokenProvider;
import com.springboot.blog.springboot_blog_rest_api.services.AuthService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public JwtAuthResponse authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        return new JwtAuthResponse(token);
    }

    @Transactional
    @Override
    public ResponseEntity<String> registerUser(SignupDto signupDto) {
        logger.info("Creating a new user...");
        if (userRepository.existsByUsername(signupDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken", HttpStatus.BAD_REQUEST);
        }
        logger.info("Passed username and email checks");
        logger.info("Creating user object...");
        User user = new User();
        user.setName(signupDto.getName());
        user.setUsername(signupDto.getUsername());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        logger.info("Searching for default role to assign the user...");
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    logger.info("Role not found, creating a new one...");
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole); // Ensure role is persisted first
                });

        logger.info("Assigning role to user...");
        user.getRoles().add(role); // Assign role after ensuring persistence
        userRepository.save(user);
        logger.info("User registered successfully");

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}