package com.example.jwtapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.jwtapi.TestUtil;
import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.exception.ResourceNotFoundException;
import com.example.jwtapi.mapper.TodoMapper;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.Todo;
import com.example.jwtapi.model.User;
import com.example.jwtapi.service.impl.TodoServiceImpl;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoMapper todoMapper;
    
    @Mock
    private UserMapper userMapper;
    
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    
    @InjectMocks
    private TodoServiceImpl todoService;
    
    private User testUser;
    private Todo testTodo;
    private TodoCreateVO testTodoCreateVO;
    private TodoUpdateVO testTodoUpdateVO;
    
    @BeforeEach
    void setUp() {
        testUser = TestUtil.createTestUser();
        testTodo = TestUtil.createTestTodo(testUser.getId());
        testTodoCreateVO = TestUtil.createTestTodoCreateVO();
        testTodoUpdateVO = TestUtil.createTestTodoUpdateVO();
    }
    
    @Test
    @DisplayName("Todo 생성 테스트")
    void testCreateTodo() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        
        doAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(1L);
            return null;
        }).when(todoMapper).insertTodo(any(Todo.class));
        
        // When
        TodoDTO result = todoService.createTodo("testuser", testTodoCreateVO);
        
        // Then
        assertNotNull(result);
        assertEquals(testTodoCreateVO.getTitle(), result.getTitle());
        assertEquals(testTodoCreateVO.getContent(), result.getContent());
        assertEquals(testTodoCreateVO.isCompleted(), result.isCompleted());
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(1L, result.getId());
        verify(todoMapper, times(1)).insertTodo(any(Todo.class));
    }
    
    @Test
    @DisplayName("Todo 생성 실패 - 사용자 없음")
    void testCreateTodoUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.createTodo("unknownuser", testTodoCreateVO));
    }
    
    @Test
    @DisplayName("Todo 목록 조회 테스트")
    void testGetAllTodos() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findAllByUserId(anyLong())).thenReturn(Arrays.asList(testTodo));
        
        // When
        List<TodoDTO> result = todoService.getAllTodos("testuser");
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTodo.getId(), result.get(0).getId());
        assertEquals(testTodo.getTitle(), result.get(0).getTitle());
        assertEquals(testTodo.getContent(), result.get(0).getContent());
        assertEquals(testTodo.isCompleted(), result.get(0).isCompleted());
        assertEquals(testTodo.getUserId(), result.get(0).getUserId());
    }
    
    @Test
    @DisplayName("Todo 목록 조회 실패 - 사용자 없음")
    void testGetAllTodosUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.getAllTodos("unknownuser"));
    }
    
    @Test
    @DisplayName("Todo 상세 조회 테스트")
    void testGetTodoById() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(testTodo);
        
        // When
        TodoDTO result = todoService.getTodoById("testuser", 1L);
        
        // Then
        assertNotNull(result);
        assertEquals(testTodo.getId(), result.getId());
        assertEquals(testTodo.getTitle(), result.getTitle());
        assertEquals(testTodo.getContent(), result.getContent());
        assertEquals(testTodo.isCompleted(), result.isCompleted());
        assertEquals(testTodo.getUserId(), result.getUserId());
    }
    
    @Test
    @DisplayName("Todo 상세 조회 실패 - 사용자 없음")
    void testGetTodoByIdUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.getTodoById("unknownuser", 1L));
    }
    
    @Test
    @DisplayName("Todo 상세 조회 실패 - Todo 없음")
    void testGetTodoByIdTodoNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.getTodoById("testuser", 999L));
    }
    
    @Test
    @DisplayName("Todo 업데이트 테스트")
    void testUpdateTodo() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(testTodo);
        doNothing().when(todoMapper).updateTodo(any(Todo.class));
        
        // When
        TodoDTO result = todoService.updateTodo("testuser", 1L, testTodoUpdateVO);
        
        // Then
        assertNotNull(result);
        assertEquals(testTodo.getId(), result.getId());
        assertEquals(testTodoUpdateVO.getTitle(), result.getTitle());
        assertEquals(testTodoUpdateVO.getContent(), result.getContent());
        assertEquals(testTodoUpdateVO.getCompleted(), result.isCompleted());
        verify(todoMapper, times(1)).updateTodo(any(Todo.class));
    }
    
    @Test
    @DisplayName("Todo 업데이트 실패 - 사용자 없음")
    void testUpdateTodoUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.updateTodo("unknownuser", 1L, testTodoUpdateVO));
    }
    
    @Test
    @DisplayName("Todo 업데이트 실패 - Todo 없음")
    void testUpdateTodoTodoNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.updateTodo("testuser", 999L, testTodoUpdateVO));
    }
    
    @Test
    @DisplayName("Todo 삭제 테스트")
    void testDeleteTodo() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(testTodo);
        doNothing().when(todoMapper).deleteTodo(anyLong(), anyLong());
        
        // When
        todoService.deleteTodo("testuser", 1L);
        
        // Then
        verify(todoMapper, times(1)).deleteTodo(1L, testUser.getId());
    }
    
    @Test
    @DisplayName("Todo 삭제 실패 - 사용자 없음")
    void testDeleteTodoUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.deleteTodo("unknownuser", 1L));
    }
    
    @Test
    @DisplayName("Todo 삭제 실패 - Todo 없음")
    void testDeleteTodoTodoNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.findByIdAndUserId(anyLong(), anyLong())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.deleteTodo("testuser", 999L));
    }
    
    @Test
    @DisplayName("Todo 검색 테스트")
    void testSearchTodos() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(todoMapper.searchTodos(anyLong(), anyString(), any())).thenReturn(Arrays.asList(testTodo));
        
        TodoSearchVO searchVO = new TodoSearchVO();
        searchVO.setKeyword("Test");
        searchVO.setCompleted(false);
        
        // When
        List<TodoDTO> result = todoService.searchTodos("testuser", searchVO);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTodo.getId(), result.get(0).getId());
        assertEquals(testTodo.getTitle(), result.get(0).getTitle());
        verify(todoMapper, times(1)).searchTodos(testUser.getId(), searchVO.getKeyword(), searchVO.getCompleted());
    }
    
    @Test
    @DisplayName("Todo 검색 실패 - 사용자 없음")
    void testSearchTodosUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        TodoSearchVO searchVO = new TodoSearchVO();
        searchVO.setKeyword("Test");
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> todoService.searchTodos("unknownuser", searchVO));
    }
} 