package com.example.jwtapi;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.jwtapi.model.Todo;
import com.example.jwtapi.model.User;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

public class TestUtil {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public static User createTestUser() {
        return User.builder()
                .id(1L)
                .username("testuser")
                .password(passwordEncoder.encode("password"))
                .email("test@example.com")
                .name("Test User")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    public static UserSignupVO createTestUserSignupVO() {
        UserSignupVO vo = new UserSignupVO();
        vo.setUsername("testuser");
        vo.setPassword("password");
        vo.setEmail("test@example.com");
        vo.setName("Test User");
        return vo;
    }
    
    public static UserLoginVO createTestUserLoginVO() {
        UserLoginVO vo = new UserLoginVO();
        vo.setUsername("testuser");
        vo.setPassword("password");
        return vo;
    }
    
    public static UserUpdateVO createTestUserUpdateVO() {
        UserUpdateVO vo = new UserUpdateVO();
        vo.setEmail("updated@example.com");
        vo.setName("Updated User");
        return vo;
    }
    
    public static Todo createTestTodo(Long userId) {
        return Todo.builder()
                .id(1L)
                .userId(userId)
                .title("Test Todo")
                .content("Test Content")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    public static TodoCreateVO createTestTodoCreateVO() {
        TodoCreateVO vo = new TodoCreateVO();
        vo.setTitle("Test Todo");
        vo.setContent("Test Content");
        vo.setCompleted(false);
        return vo;
    }
    
    public static TodoUpdateVO createTestTodoUpdateVO() {
        TodoUpdateVO vo = new TodoUpdateVO();
        vo.setTitle("Updated Todo");
        vo.setContent("Updated Content");
        vo.setCompleted(true);
        return vo;
    }
} 