package com.example.gaurdsocial.mappers;

import com.example.gaurdsocial.dto.BotResponseDto;
import com.example.gaurdsocial.entity.Bot;

public class BotMapper {
    public static BotResponseDto mapToBotResponse(Bot bot) {
        BotResponseDto botResponseDto = new BotResponseDto();
        botResponseDto.setUsername(bot.getUsername());
        botResponseDto.setPersona_description(bot.getPersona_description());
        botResponseDto.setAuthorId(bot.getAutherId());
        return botResponseDto;
    }
}
