package com.example.jwtapi.vo.todo;

import lombok.Data;

@Data
public class TodoUpdateVO {
    private String title;
    private String content;
    private Boolean completed;
} 