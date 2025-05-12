package com.example.jwtapi.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.JwtException;

class JwtTokenProviderTest {

    @Mock
    private CustomUserDetailsService userDetailsService;
    
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;
    
    private String secretKey = "testsecretkeytestsecretkeytestsecretkey12345";
    private long validityInMilliseconds = 3600000; // 1시간
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", validityInMilliseconds);
        jwtTokenProvider.init();
    }
    
    @Test
    void testCreateToken() {
        // given
        String username = "testuser";
        Long userId = 1L;
        
        // when
        String token = jwtTokenProvider.createToken(username, userId);
        
        // then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }
    
    @Test
    void testGetUsername() {
        // given
        String username = "testuser";
        Long userId = 1L;
        String token = jwtTokenProvider.createToken(username, userId);
        
        // when
        String extractedUsername = jwtTokenProvider.getUsername(token);
        
        // then
        assertEquals(username, extractedUsername);
    }
    
    @Test
    void testGetAuthentication() {
        // given
        String username = "testuser";
        Long userId = 1L;
        String token = jwtTokenProvider.createToken(username, userId);
        
        // UserDetailsService 모의 설정
        UserDetails userDetails = new User(
            username,
            "password",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        
        // when
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        
        // then
        assertNotNull(authentication);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
    }
    
    @Test
    void testValidateToken_ValidToken() {
        // given
        String username = "testuser";
        Long userId = 1L;
        String token = jwtTokenProvider.createToken(username, userId);
        
        // when
        boolean isValid = jwtTokenProvider.validateToken(token);
        
        // then
        assertTrue(isValid);
    }
    
    @Test
    void testValidateToken_ExpiredToken() {
        // given
        String username = "testuser";
        Long userId = 1L;
        
        // 만료된 토큰 생성
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", -3600000); // -1시간 (이미 만료됨)
        String expiredToken = jwtTokenProvider.createToken(username, userId);
        
        // when
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);
        
        // then
        assertFalse(isValid);
    }
    
    @Test
    void testValidateToken_InvalidToken() {
        // given
        String invalidToken = "invalid.token.string";
        
        // when
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        
        // then
        assertFalse(isValid);
    }
} 