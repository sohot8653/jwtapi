package com.example.jwtapi.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void testUserModel() {
        User user = new User();
        
        // Setter 테스트
        Long id = 1L;
        String username = "testuser";
        String password = "password";
        String email = "test@example.com";
        String name = "Test User";
        String googleId = "google123";
        String profileImage = "profile.jpg";
        String authProvider = "google";
        LocalDateTime now = LocalDateTime.now();
        
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        user.setGoogleId(googleId);
        user.setProfileImage(profileImage);
        user.setAuthProvider(authProvider);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        
        // Getter 테스트
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(googleId, user.getGoogleId());
        assertEquals(profileImage, user.getProfileImage());
        assertEquals(authProvider, user.getAuthProvider());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        
        // toString 테스트
        assertNotNull(user.toString());
        assertTrue(user.toString().contains(username));
        assertTrue(user.toString().contains(email));
        
        // Builder 테스트
        User user2 = User.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .name(name)
                .googleId(googleId)
                .profileImage(profileImage)
                .authProvider(authProvider)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(id, user2.getId());
        assertEquals(username, user2.getUsername());
        assertEquals(password, user2.getPassword());
        assertEquals(email, user2.getEmail());
        assertEquals(name, user2.getName());
        assertEquals(googleId, user2.getGoogleId());
        assertEquals(profileImage, user2.getProfileImage());
        assertEquals(authProvider, user2.getAuthProvider());
        assertEquals(now, user2.getCreatedAt());
        assertEquals(now, user2.getUpdatedAt());
        
        // equals 및 hashCode 테스트
        User user3 = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
        
        User user4 = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .build();
        
        assertEquals(user3, user4);
        assertEquals(user3.hashCode(), user4.hashCode());
        
        // 생성자 테스트
        User user5 = new User(id, username, password, email, name, googleId, profileImage, authProvider, now, now);
        assertEquals(id, user5.getId());
        assertEquals(username, user5.getUsername());
        assertEquals(password, user5.getPassword());
        assertEquals(email, user5.getEmail());
        assertEquals(name, user5.getName());
        assertEquals(googleId, user5.getGoogleId());
        assertEquals(profileImage, user5.getProfileImage());
        assertEquals(authProvider, user5.getAuthProvider());
        assertEquals(now, user5.getCreatedAt());
        assertEquals(now, user5.getUpdatedAt());
    }
    
    @Test
    void testTodoModel() {
        Todo todo = new Todo();
        
        // Setter 테스트
        Long id = 1L;
        Long userId = 1L;
        String title = "Test Todo";
        String content = "This is a test";
        boolean completed = false;
        LocalDateTime now = LocalDateTime.now();
        
        todo.setId(id);
        todo.setUserId(userId);
        todo.setTitle(title);
        todo.setContent(content);
        todo.setCompleted(completed);
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        
        // Getter 테스트
        assertEquals(id, todo.getId());
        assertEquals(userId, todo.getUserId());
        assertEquals(title, todo.getTitle());
        assertEquals(content, todo.getContent());
        assertEquals(completed, todo.isCompleted());
        assertEquals(now, todo.getCreatedAt());
        assertEquals(now, todo.getUpdatedAt());
        
        // toString 테스트
        assertNotNull(todo.toString());
        assertTrue(todo.toString().contains(title));
        assertTrue(todo.toString().contains(content));
        
        // Builder 테스트
        Todo todo2 = Todo.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .content(content)
                .completed(completed)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals(id, todo2.getId());
        assertEquals(userId, todo2.getUserId());
        assertEquals(title, todo2.getTitle());
        assertEquals(content, todo2.getContent());
        assertEquals(completed, todo2.isCompleted());
        assertEquals(now, todo2.getCreatedAt());
        assertEquals(now, todo2.getUpdatedAt());
        
        // equals 및 hashCode 테스트
        Todo todo3 = Todo.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .build();
        
        Todo todo4 = Todo.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .build();
        
        assertEquals(todo3, todo4);
        assertEquals(todo3.hashCode(), todo4.hashCode());
        
        // 생성자 테스트
        Todo todo5 = new Todo(id, userId, title, content, completed, now, now);
        assertEquals(id, todo5.getId());
        assertEquals(userId, todo5.getUserId());
        assertEquals(title, todo5.getTitle());
        assertEquals(content, todo5.getContent());
        assertEquals(completed, todo5.isCompleted());
        assertEquals(now, todo5.getCreatedAt());
        assertEquals(now, todo5.getUpdatedAt());
    }
} 