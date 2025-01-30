package com.springboot.blog.springboot_blog_rest_api.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Data // Lombok annotation to create all the getters, setters, equals, hash, and
// toString methods based on the fields
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity // JPA annotation to make this object ready for storage in a JPA-based data
        // store
@Table(name = "posts", uniqueConstraints = {
                @UniqueConstraint(columnNames = "title")
})
public class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "title", nullable = false)
        private String title;
        @Column(name = "description", nullable = false)
        private String description;
        @Column(name = "content", nullable = false)
        private String content;

        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
        // orphanRemoval = true means that if a comment is removed from the comments
        // list of a post, it will be deleted from the database
        private Set<Comment> comments = new HashSet<>();

        // new fields
        @CreationTimestamp
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        @Column(name = "updated_at", nullable = false)
        private LocalDateTime updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = true)
        private User owner;

}
