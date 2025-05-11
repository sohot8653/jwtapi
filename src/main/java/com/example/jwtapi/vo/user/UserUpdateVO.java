package com.example.jwtapi.vo.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateVO {
    private String password;
    
    @Email(message = "Email should be valid")
    private String email;
    
    private String name;
} 