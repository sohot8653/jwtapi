package com.example.jwtapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import com.example.jwtapi.AbstractTestBase;
import com.example.jwtapi.TestUtil;
import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.service.TodoService;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;

class TodoControllerTest extends AbstractTestBase {

    @MockBean
    private TodoService todoService;
    
    private TodoDTO testTodoDTO;
    private TodoCreateVO testTodoCreateVO;
    private TodoUpdateVO testTodoUpdateVO;
    
    @BeforeEach
    void setUp() {
        testTodoDTO = new TodoDTO();
        testTodoDTO.setId(1L);
        testTodoDTO.setUserId(1L);
        testTodoDTO.setTitle("Test Todo");
        testTodoDTO.setContent("Test Content");
        testTodoDTO.setCompleted(false);
        
        testTodoCreateVO = TestUtil.createTestTodoCreateVO();
        testTodoUpdateVO = TestUtil.createTestTodoUpdateVO();
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 생성 API 테스트")
    void testCreateTodo() throws Exception {
        // Given
        when(todoService.createTodo(anyString(), any(TodoCreateVO.class))).thenReturn(testTodoDTO);
        
        // When
        ResultActions result = mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTodoCreateVO)));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.id").value(testTodoDTO.getId()))
              .andExpect(jsonPath("$.data.title").value(testTodoDTO.getTitle()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 목록 조회 API 테스트")
    void testGetAllTodos() throws Exception {
        // Given
        List<TodoDTO> todos = Arrays.asList(testTodoDTO);
        when(todoService.getAllTodos(anyString())).thenReturn(todos);
        
        // When
        ResultActions result = mockMvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data[0].id").value(testTodoDTO.getId()))
              .andExpect(jsonPath("$.data[0].title").value(testTodoDTO.getTitle()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 상세 조회 API 테스트")
    void testGetTodoById() throws Exception {
        // Given
        when(todoService.getTodoById(anyString(), anyLong())).thenReturn(testTodoDTO);
        
        // When
        ResultActions result = mockMvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.id").value(testTodoDTO.getId()))
              .andExpect(jsonPath("$.data.title").value(testTodoDTO.getTitle()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 수정 API 테스트")
    void testUpdateTodo() throws Exception {
        // Given
        TodoDTO updatedTodoDTO = new TodoDTO();
        updatedTodoDTO.setId(1L);
        updatedTodoDTO.setUserId(1L);
        updatedTodoDTO.setTitle(testTodoUpdateVO.getTitle());
        updatedTodoDTO.setContent(testTodoUpdateVO.getContent());
        updatedTodoDTO.setCompleted(testTodoUpdateVO.getCompleted());
        
        when(todoService.updateTodo(anyString(), anyLong(), any(TodoUpdateVO.class))).thenReturn(updatedTodoDTO);
        
        // When
        ResultActions result = mockMvc.perform(put("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTodoUpdateVO)));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.title").value(testTodoUpdateVO.getTitle()))
              .andExpect(jsonPath("$.data.content").value(testTodoUpdateVO.getContent()))
              .andExpect(jsonPath("$.data.completed").value(testTodoUpdateVO.getCompleted()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 삭제 API 테스트")
    void testDeleteTodo() throws Exception {
        // When
        ResultActions result = mockMvc.perform(delete("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Todo 검색 API 테스트")
    void testSearchTodos() throws Exception {
        // Given
        List<TodoDTO> todos = Arrays.asList(testTodoDTO);
        when(todoService.searchTodos(anyString(), any(TodoSearchVO.class))).thenReturn(todos);
        
        // When
        ResultActions result = mockMvc.perform(get("/todos/search")
                .param("keyword", "Test")
                .param("completed", "false")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data[0].id").value(testTodoDTO.getId()))
              .andExpect(jsonPath("$.data[0].title").value(testTodoDTO.getTitle()));
    }
} 