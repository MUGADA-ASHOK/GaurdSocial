package com.example.gaurdsocial.entity;


// id, author_id (can be User or Bot), content, created_at

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;
    private Long authorId;
    private String authorType;
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    List<Like> likes = new ArrayList<>();
}
