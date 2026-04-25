package com.example.gaurdsocial.service.impl;

import com.example.gaurdsocial.dto.BotRequestDto;
import com.example.gaurdsocial.dto.BotResponseDto;
import com.example.gaurdsocial.dto.UserRequestDto;
import com.example.gaurdsocial.entity.Bot;
import com.example.gaurdsocial.mappers.BotMapper;
import com.example.gaurdsocial.repository.BotRepository;
import com.example.gaurdsocial.service.BotService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BotServiceImpl implements BotService {
    private final BotRepository botRepository;
     public BotResponseDto registerBot(BotRequestDto botDto) {
         Bot bot = new Bot();
         bot.setUsername(botDto.getUsername());
         bot.setPersona_description(botDto.getPersona_description());
         botRepository.save(bot);
         return BotMapper.mapToBotResponse(bot);
    }
}
