package com.example.gaurdsocial.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeRequest {
    private Long authorId;
    private String authorType;
}
