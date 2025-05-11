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
public class TodoDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 