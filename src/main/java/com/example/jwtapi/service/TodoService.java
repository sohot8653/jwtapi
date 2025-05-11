package com.example.jwtapi.service;

import java.util.List;

import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;

public interface TodoService {
    TodoDTO createTodo(String username, TodoCreateVO todoCreateVO);
    List<TodoDTO> getAllTodos(String username);
    TodoDTO getTodoById(String username, Long id);
    TodoDTO updateTodo(String username, Long id, TodoUpdateVO todoUpdateVO);
    void deleteTodo(String username, Long id);
    List<TodoDTO> searchTodos(String username, TodoSearchVO todoSearchVO);
} 