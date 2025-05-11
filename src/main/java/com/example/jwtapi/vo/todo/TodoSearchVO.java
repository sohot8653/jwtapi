package com.example.jwtapi.vo.todo;

import lombok.Data;

@Data
public class TodoSearchVO {
    private String keyword;
    private Boolean completed;
} 