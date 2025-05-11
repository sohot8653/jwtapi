package com.example.jwtapi.mapper;

import java.util.List;

import com.example.jwtapi.model.Todo;
import org.apache.ibatis.annotations.Param;

public interface TodoMapper {
    void insertTodo(Todo todo);
    List<Todo> findAllByUserId(Long userId);
    Todo findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    void updateTodo(Todo todo);
    void deleteTodo(@Param("id") Long id, @Param("userId") Long userId);
    List<Todo> searchTodos(@Param("userId") Long userId, @Param("keyword") String keyword, @Param("completed") Boolean completed);
} 