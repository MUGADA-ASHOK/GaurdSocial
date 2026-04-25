package com.example.gaurdsocial.service;

import com.example.gaurdsocial.dto.BotRequestDto;
import com.example.gaurdsocial.dto.BotResponseDto;

public interface BotService {
    BotResponseDto registerBot(BotRequestDto bot);
}
