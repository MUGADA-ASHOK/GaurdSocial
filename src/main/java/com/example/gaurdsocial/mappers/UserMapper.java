package com.example.gaurdsocial.mappers;

import com.example.gaurdsocial.dto.UserResponseDto;
import com.example.gaurdsocial.entity.User;

public class UserMapper {
    public static UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setAuthorId(user.getAuthorId());
        return userResponseDto;
    }
}
