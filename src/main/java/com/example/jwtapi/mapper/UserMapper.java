package com.example.jwtapi.mapper;

import com.example.jwtapi.model.User;

public interface UserMapper {
    void insertUser(User user);
    User findByUsername(String username);
    User findById(Long id);
    User findByGoogleId(String googleId);
    User findByEmail(String email);
    void updateUser(User user);
    void deleteUser(Long id);
} 