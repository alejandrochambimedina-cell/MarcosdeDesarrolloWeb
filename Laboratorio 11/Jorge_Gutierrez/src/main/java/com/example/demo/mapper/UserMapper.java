package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}