package com.example.jwtapi.vo.todo;

import lombok.Data;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class TodoSearchVO {
    @Parameter(description = "검색 키워드 (제목 또는 내용)", required = false)
    private String keyword;
    
    @Parameter(description = "완료 상태", required = false)
    private Boolean completed;
} 