package com.example.jwtapi.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwtapi.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String token = jwtTokenProvider.resolveToken(request);
            
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // JWT 예외 처리
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 