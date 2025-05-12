package com.example.jwtapi.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String googleId;     // Google OAuth2 사용자 ID
    private String profileImage; // 프로필 이미지 URL
    private String authProvider; // 인증 제공자 (GOOGLE, LOCAL)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 