package com.example.jwtapi.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.jwtapi.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final List<RequestMatcher> permitAllRequestMatchers;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper, 
                                 RequestMatcher... permitAllRequestMatchers) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.permitAllRequestMatchers = Arrays.asList(permitAllRequestMatchers);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        log.info("JwtAuthenticationFilter processing request: {}", path);
        
        try {
            String token = jwtTokenProvider.resolveToken(request);
            
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication successful for path: {}", path);
            } else {
                log.info("No valid token found for path: {}", path);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            // JWT 예외 처리
            log.error("JWT Exception for path {}: {}", path, e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            log.error("Authentication error for path {}: {}", path, e.getMessage());
            sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean shouldSkip = permitAllRequestMatchers.stream()
                .anyMatch(matcher -> {
                    boolean matches = matcher.matches(request);
                    if (matcher instanceof AntPathRequestMatcher) {
                        log.info("Checking path: {} against pattern: {}, matches: {}", 
                                path, ((AntPathRequestMatcher) matcher).getPattern(), matches);
                    }
                    return matches;
                });
        
        log.info("Path: {}, Should skip filter: {}", path, shouldSkip);
        return shouldSkip;
    }
    
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        ApiResponse<Object> apiResponse = ApiResponse.error(message);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 