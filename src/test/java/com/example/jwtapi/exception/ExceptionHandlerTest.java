package com.example.jwtapi.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.jwtapi.dto.ApiResponse;

class ExceptionHandlerTest {

    @Test
    void testResourceNotFoundException() {
        // 첫 번째 생성자 테스트
        String errorMessage = "Resource not found";
        ResourceNotFoundException ex1 = new ResourceNotFoundException(errorMessage);
        assertEquals(errorMessage, ex1.getMessage());
        
        // 두 번째 생성자 테스트
        String resourceName = "User";
        String fieldName = "id";
        Long fieldValue = 1L;
        ResourceNotFoundException ex2 = new ResourceNotFoundException(resourceName, fieldName, fieldValue);
        
        String expectedMessage = String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue);
        assertEquals(expectedMessage, ex2.getMessage());
    }
    
    @Test
    void testGlobalExceptionHandler() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        
        // ResourceNotFoundException 처리 테스트
        String errorMessage = "Resource not found";
        ResourceNotFoundException rnfEx = new ResourceNotFoundException(errorMessage);
        ResponseEntity<ApiResponse<Object>> rnfResponse = handler.handleResourceNotFoundException(rnfEx);
        
        assertEquals(HttpStatus.NOT_FOUND, rnfResponse.getStatusCode());
        assertFalse(rnfResponse.getBody().isSuccess());
        assertEquals(errorMessage, rnfResponse.getBody().getMessage());
        
        // BadCredentialsException 처리 테스트
        BadCredentialsException bcEx = new BadCredentialsException("Bad credentials");
        ResponseEntity<ApiResponse<Object>> bcResponse = handler.handleBadCredentialsException(bcEx);
        
        assertEquals(HttpStatus.UNAUTHORIZED, bcResponse.getStatusCode());
        assertFalse(bcResponse.getBody().isSuccess());
        assertEquals("잘못된 인증 정보입니다.", bcResponse.getBody().getMessage());
        
        // 일반 Exception 처리 테스트
        Exception ex = new RuntimeException("Test exception");
        ResponseEntity<ApiResponse<Object>> exResponse = handler.handleGlobalException(ex);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exResponse.getStatusCode());
        assertFalse(exResponse.getBody().isSuccess());
        assertTrue(exResponse.getBody().getMessage().contains("Test exception"));
    }
    
    @Test
    void testMethodArgumentNotValidException() throws Exception {
        // MethodArgumentNotValidException mock 생성
        MethodArgumentNotValidException mockEx = mock(MethodArgumentNotValidException.class);
        BindingResult mockResult = mock(BindingResult.class);
        
        when(mockEx.getBindingResult()).thenReturn(mockResult);
        
        // FieldError mock 생성
        FieldError mockError1 = new FieldError("object", "username", "Username is required");
        FieldError mockError2 = new FieldError("object", "email", "Email is invalid");
        
        when(mockResult.getAllErrors()).thenReturn(java.util.Arrays.asList(mockError1, mockError2));
        
        // 테스트
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<ApiResponse<Map<String, String>>> response = 
                handler.handleValidationExceptions(mockEx);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("유효성 검사 실패", response.getBody().getMessage());
        
        Map<String, String> errors = response.getBody().getData();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("Username is required", errors.get("username"));
        assertEquals("Email is invalid", errors.get("email"));
    }
} 