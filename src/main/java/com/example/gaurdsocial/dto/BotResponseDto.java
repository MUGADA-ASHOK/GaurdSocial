package com.example.gaurdsocial.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BotResponseDto {
    private Long authorId;
    private String authorType="bot";
    private String username;
    private String persona_description;
}
