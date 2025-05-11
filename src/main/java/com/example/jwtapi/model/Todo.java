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
public class Todo {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 