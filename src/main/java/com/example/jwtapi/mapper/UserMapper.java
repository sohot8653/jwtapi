package com.example.jwtapi.mapper;

import com.example.jwtapi.model.User;

public interface UserMapper {
    void insertUser(User user);
    User findByUsername(String username);
    User findById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
} 