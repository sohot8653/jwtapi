package com.example.jwtapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String googleId;
    private String profileImage;
    private String authProvider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 