package com.example.gaurdsocial.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentRequest {
    private Long parentCommentId;
    private Long authorId;
    private String authorType;
    private String content;
}
