package com.example.gaurdsocial.service.impl;


import com.example.gaurdsocial.dto.UserRequestDto;
import com.example.gaurdsocial.dto.UserResponseDto;
import com.example.gaurdsocial.entity.User;
import com.example.gaurdsocial.exception.ResourceNotFoundException;
import com.example.gaurdsocial.repository.UserRepository;
import com.example.gaurdsocial.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public UserResponseDto registerUser(UserRequestDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(userDto.getUsername());
        userResponseDto.setAuthorId(user.getAuthorId());
        return userResponseDto;
    }
}
