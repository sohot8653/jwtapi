package com.example.jwtapi.vo.todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoCreateVO {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String content;
    
    private boolean completed;
} 