package com.example.gaurdsocial.service;

import com.example.gaurdsocial.dto.BotRequestDto;
import com.example.gaurdsocial.dto.UserRequestDto;
import com.example.gaurdsocial.dto.UserResponseDto;

public interface UserService {
     UserResponseDto registerUser(UserRequestDto userDto);
}
