package com.example.gaurdsocial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long autherId;
    private String username;
    private String persona_description;
    private Boolean isUser=false;
}
