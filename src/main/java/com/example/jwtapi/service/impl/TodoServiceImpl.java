package com.example.jwtapi.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.exception.ResourceNotFoundException;
import com.example.jwtapi.mapper.TodoMapper;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.Todo;
import com.example.jwtapi.model.User;
import com.example.jwtapi.service.TodoService;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoSearchVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;

    @Override
    public TodoDTO createTodo(String username, TodoCreateVO todoCreateVO) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        // VO -> DTO 변환
        TodoDTO todoDTO = modelMapper.map(todoCreateVO, TodoDTO.class);
        todoDTO.setUserId(user.getId());
        todoDTO.setCreatedAt(LocalDateTime.now());
        todoDTO.setUpdatedAt(LocalDateTime.now());
        
        // DTO -> Entity 변환
        Todo todoEntity = modelMapper.map(todoDTO, Todo.class);
        
        // 저장
        todoMapper.insertTodo(todoEntity);
        
        // ID 설정
        todoDTO.setId(todoEntity.getId());
        
        return todoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoDTO> getAllTodos(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        List<Todo> todos = todoMapper.findAllByUserId(user.getId());
        
        return todos.stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TodoDTO getTodoById(String username, Long id) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        Todo todo = todoMapper.findByIdAndUserId(id, user.getId());
        if (todo == null) {
            throw new ResourceNotFoundException("Todo", "id", id);
        }
        
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public TodoDTO updateTodo(String username, Long id, TodoUpdateVO todoUpdateVO) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        Todo todo = todoMapper.findByIdAndUserId(id, user.getId());
        if (todo == null) {
            throw new ResourceNotFoundException("Todo", "id", id);
        }
        
        // 변경 사항 적용
        if (todoUpdateVO.getTitle() != null) {
            todo.setTitle(todoUpdateVO.getTitle());
        }
        
        if (todoUpdateVO.getContent() != null) {
            todo.setContent(todoUpdateVO.getContent());
        }
        
        if (todoUpdateVO.getCompleted() != null) {
            todo.setCompleted(todoUpdateVO.getCompleted());
        }
        
        todo.setUpdatedAt(LocalDateTime.now());
        
        // 업데이트
        todoMapper.updateTodo(todo);
        
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public void deleteTodo(String username, Long id) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        Todo todo = todoMapper.findByIdAndUserId(id, user.getId());
        if (todo == null) {
            throw new ResourceNotFoundException("Todo", "id", id);
        }
        
        todoMapper.deleteTodo(id, user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TodoDTO> searchTodos(String username, TodoSearchVO todoSearchVO) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        List<Todo> todos = todoMapper.searchTodos(
            user.getId(), 
            todoSearchVO.getKeyword(), 
            todoSearchVO.getCompleted()
        );
        
        return todos.stream()
                .map(todo -> modelMapper.map(todo, TodoDTO.class))
                .collect(Collectors.toList());
    }
} 