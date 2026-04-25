package com.example.gaurdsocial.controllers;

import com.example.gaurdsocial.dto.BotRequestDto;
import com.example.gaurdsocial.dto.BotResponseDto;
import com.example.gaurdsocial.service.BotService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bot")
public class BotController {
    private final BotService botService;
    @PostMapping
    public ResponseEntity<BotResponseDto> registerBot(@RequestBody BotRequestDto botDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(botService.registerBot(botDto));
    }
}
