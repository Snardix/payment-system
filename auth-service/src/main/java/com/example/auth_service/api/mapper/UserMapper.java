package com.example.auth_service.api.mapper;

import com.example.auth_service.api.dto.RegisterRequest;
import com.example.auth_service.api.dto.UserDto;
import com.example.auth_service.domain.enums.Role;
import com.example.auth_service.domain.model.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserMapper {

    public static User fromRegisterRequest(RegisterRequest req) {
        Role role = Role.USER;
        if (req.getRole() != null) {
            try {
                role = Role.valueOf(req.getRole().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                role = Role.USER;
            }
        }
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(req.getEmail());
        user.setPassword(req.getPassword());
        user.setRole(role);
        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getId(), user.getEmail(), user.getRole());
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
