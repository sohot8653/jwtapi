package com.example.jwtapi.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class DtoTest {

    @Test
    void testUserDTO() {
        UserDTO userDTO = new UserDTO();
        
        // Setter 테스트
        Long id = 1L;
        String username = "testuser";
        String email = "test@example.com";
        String name = "Test User";
        String googleId = "123456789";
        String profileImage = "https://example.com/profile.jpg";
        String authProvider = "GOOGLE";
        LocalDateTime now = LocalDateTime.now();
        
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        userDTO.setName(name);
        userDTO.setGoogleId(googleId);
        userDTO.setProfileImage(profileImage);
        userDTO.setAuthProvider(authProvider);
        userDTO.setCreatedAt(now);
        userDTO.setUpdatedAt(now);
        
        // Getter 테스트
        assertEquals(id, userDTO.getId());
        assertEquals(username, userDTO.getUsername());
        assertEquals(email, userDTO.getEmail());
        assertEquals(name, userDTO.getName());
        assertEquals(googleId, userDTO.getGoogleId());
        assertEquals(profileImage, userDTO.getProfileImage());
        assertEquals(authProvider, userDTO.getAuthProvider());
        assertEquals(now, userDTO.getCreatedAt());
        assertEquals(now, userDTO.getUpdatedAt());
        
        // toString 테스트
        assertNotNull(userDTO.toString());
        assertTrue(userDTO.toString().contains(username));
        assertTrue(userDTO.toString().contains(email));
        
        // 생성자 테스트
        UserDTO dto2 = new UserDTO(id, username, "password", email, name, googleId, profileImage, authProvider, now, now);
        assertEquals(id, dto2.getId());
        assertEquals(username, dto2.getUsername());
        assertEquals(email, dto2.getEmail());
        assertEquals(name, dto2.getName());
        assertEquals(now, dto2.getCreatedAt());
        assertEquals(now, dto2.getUpdatedAt());
        
        // Builder 테스트
        UserDTO dto3 = UserDTO.builder()
                .id(id)
                .username(username)
                .email(email)
                .name(name)
                .googleId(googleId)
                .profileImage(profileImage)
                .authProvider(authProvider)
                .createdAt(now)
                .updatedAt(now)
                .build();
        assertEquals(id, dto3.getId());
        assertEquals(username, dto3.getUsername());
        assertEquals(email, dto3.getEmail());
        assertEquals(name, dto3.getName());
        assertEquals(googleId, dto3.getGoogleId());
        assertEquals(profileImage, dto3.getProfileImage());
        assertEquals(authProvider, dto3.getAuthProvider());
        assertEquals(now, dto3.getCreatedAt());
        assertEquals(now, dto3.getUpdatedAt());
        
        // equals 및 hashCode 테스트
        UserDTO dto4 = UserDTO.builder()
                .id(id)
                .username(username)
                .email(email)
                .name(name)
                .build();
        UserDTO dto5 = UserDTO.builder()
                .id(id)
                .username(username)
                .email(email)
                .name(name)
                .build();
        
        assertEquals(dto4, dto5);
        assertEquals(dto4.hashCode(), dto5.hashCode());
    }
    
    @Test
    void testTodoDTO() {
        TodoDTO todoDTO = new TodoDTO();
        
        // Setter 테스트
        Long id = 1L;
        Long userId = 1L;
        String title = "Test Todo";
        String content = "Test Content";
        boolean completed = false;
        LocalDateTime now = LocalDateTime.now();
        
        todoDTO.setId(id);
        todoDTO.setUserId(userId);
        todoDTO.setTitle(title);
        todoDTO.setContent(content);
        todoDTO.setCompleted(completed);
        todoDTO.setCreatedAt(now);
        todoDTO.setUpdatedAt(now);
        
        // Getter 테스트
        assertEquals(id, todoDTO.getId());
        assertEquals(userId, todoDTO.getUserId());
        assertEquals(title, todoDTO.getTitle());
        assertEquals(content, todoDTO.getContent());
        assertEquals(completed, todoDTO.isCompleted());
        assertEquals(now, todoDTO.getCreatedAt());
        assertEquals(now, todoDTO.getUpdatedAt());
        
        // toString 테스트
        assertNotNull(todoDTO.toString());
        assertTrue(todoDTO.toString().contains(title));
        assertTrue(todoDTO.toString().contains(content));
        
        // 생성자 테스트
        TodoDTO dto2 = new TodoDTO(id, userId, title, content, completed, now, now);
        assertEquals(id, dto2.getId());
        assertEquals(userId, dto2.getUserId());
        assertEquals(title, dto2.getTitle());
        assertEquals(content, dto2.getContent());
        assertEquals(completed, dto2.isCompleted());
        assertEquals(now, dto2.getCreatedAt());
        assertEquals(now, dto2.getUpdatedAt());
        
        // Builder 테스트
        TodoDTO dto3 = TodoDTO.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .content(content)
                .completed(completed)
                .createdAt(now)
                .updatedAt(now)
                .build();
        assertEquals(id, dto3.getId());
        assertEquals(userId, dto3.getUserId());
        assertEquals(title, dto3.getTitle());
        assertEquals(content, dto3.getContent());
        assertEquals(completed, dto3.isCompleted());
        assertEquals(now, dto3.getCreatedAt());
        assertEquals(now, dto3.getUpdatedAt());
        
        // equals 및 hashCode 테스트
        TodoDTO dto4 = TodoDTO.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .content(content)
                .completed(completed)
                .build();
        TodoDTO dto5 = TodoDTO.builder()
                .id(id)
                .userId(userId)
                .title(title)
                .content(content)
                .completed(completed)
                .build();
        
        assertEquals(dto4, dto5);
        assertEquals(dto4.hashCode(), dto5.hashCode());
    }
    
    @Test
    void testApiResponse() {
        // 성공 케이스
        Map<String, Object> data = new HashMap<>();
        data.put("key", "value");
        
        ApiResponse<Map<String, Object>> successResponse = ApiResponse.success("Success message", data);
        
        assertTrue(successResponse.isSuccess());
        assertEquals("Success message", successResponse.getMessage());
        assertEquals(data, successResponse.getData());
        assertNotNull(successResponse.toString());
        
        // 실패 케이스
        ApiResponse<Object> errorResponse = ApiResponse.error("Error message", null);
        
        assertFalse(errorResponse.isSuccess());
        assertEquals("Error message", errorResponse.getMessage());
        assertNull(errorResponse.getData());
        assertNotNull(errorResponse.toString());
        
        // 기본 생성자 및 설정자 테스트
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData("Test data");
        
        assertTrue(response.isSuccess());
        assertEquals("Test message", response.getMessage());
        assertEquals("Test data", response.getData());
        
        // 추가 static 메서드 테스트
        ApiResponse<String> successNoData = ApiResponse.success("Test data");
        assertTrue(successNoData.isSuccess());
        assertEquals("Test data", successNoData.getData());
        
        ApiResponse<String> errorNoMsg = ApiResponse.error("Error message");
        assertFalse(errorNoMsg.isSuccess());
        assertEquals("Error message", errorNoMsg.getMessage());
        assertNull(errorNoMsg.getData());
        
        // 전체 생성자 테스트
        ApiResponse<String> fullCtor = new ApiResponse<>(true, "Full message", "Full data");
        assertTrue(fullCtor.isSuccess());
        assertEquals("Full message", fullCtor.getMessage());
        assertEquals("Full data", fullCtor.getData());
        
        // equals 및 hashCode 테스트
        ApiResponse<String> resp1 = new ApiResponse<>(true, "msg", "data");
        ApiResponse<String> resp2 = new ApiResponse<>(true, "msg", "data");
        
        assertEquals(resp1, resp2);
        assertEquals(resp1.hashCode(), resp2.hashCode());
        
        // Builder 테스트
        ApiResponse<String> builtResp = ApiResponse.<String>builder()
                .success(true)
                .message("Builder message")
                .data("Builder data")
                .build();
        
        assertTrue(builtResp.isSuccess());
        assertEquals("Builder message", builtResp.getMessage());
        assertEquals("Builder data", builtResp.getData());
    }
    
    @Test
    void testGoogleLoginDTO() {
        // Google Login DTO 테스트 (있는 경우)
        try {
            Class<?> clazz = Class.forName("com.example.jwtapi.dto.GoogleLoginDTO");
            Object dto = clazz.getDeclaredConstructor().newInstance();
            
            // Reflection을 이용한 테스트
            java.lang.reflect.Method setMethod = clazz.getMethod("setRedirectUrl", String.class);
            setMethod.invoke(dto, "https://example.com/redirect");
            
            java.lang.reflect.Method getMethod = clazz.getMethod("getRedirectUrl");
            String url = (String) getMethod.invoke(dto);
            
            assertEquals("https://example.com/redirect", url);
        } catch (ClassNotFoundException e) {
            // 클래스가 없는 경우 스킵
            System.out.println("GoogleLoginDTO class not found, skipping test");
        } catch (Exception e) {
            fail("Exception in testing GoogleLoginDTO: " + e.getMessage());
        }
    }
    
    @Test
    void testErrorResponse() {
        // ErrorResponse 클래스 테스트 (있는 경우)
        try {
            Class<?> clazz = Class.forName("com.example.jwtapi.dto.ErrorResponse");
            Object dto = clazz.getDeclaredConstructor(String.class, int.class).newInstance("Test error", 400);
            
            // Reflection을 이용한 테스트
            java.lang.reflect.Method getMessage = clazz.getMethod("getMessage");
            String message = (String) getMessage.invoke(dto);
            
            java.lang.reflect.Method getStatus = clazz.getMethod("getStatus");
            int status = (Integer) getStatus.invoke(dto);
            
            assertEquals("Test error", message);
            assertEquals(400, status);
        } catch (ClassNotFoundException e) {
            // 클래스가 없는 경우 스킵
            System.out.println("ErrorResponse class not found, skipping test");
        } catch (Exception e) {
            fail("Exception in testing ErrorResponse: " + e.getMessage());
        }
    }
} 