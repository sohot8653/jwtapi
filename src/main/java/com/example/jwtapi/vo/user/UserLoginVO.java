package com.example.jwtapi.vo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginVO {
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
} 