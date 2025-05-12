package com.example.jwtapi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.example.jwtapi.dto.ApiResponse;
import com.example.jwtapi.service.OAuth2Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
@Tag(name = "OAuth2", description = "OAuth2 인증 관련 API")
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    
    @Value("${oauth2.authorized-redirect-uri}")
    private String redirectUri;
    
    @Operation(summary = "Google 로그인 페이지", description = "Google 로그인 페이지로 리다이렉트")
    @GetMapping("/login/google")
    public RedirectView googleLogin() {
        String googleAuthUrl = "https://accounts.google.com/o/oauth2/v2/auth";
        String scope = "email%20profile";
        
        String authUrl = googleAuthUrl 
                + "?client_id=" + clientId 
                + "&redirect_uri=" + redirectUri 
                + "&response_type=code"
                + "&scope=" + scope;
        
        return new RedirectView(authUrl);
    }
    
    @Operation(summary = "Google 로그인 콜백", description = "Google에서 리다이렉트된 코드로 인증 처리 후 JWT 토큰 발급")
    @GetMapping("/callback")
    public void callback(
            @Parameter(description = "Google에서 제공한 인증 코드") @RequestParam String code,
            HttpServletResponse response) throws Exception {
        String token = oAuth2Service.processOAuth2Login(code);
        
        // 프론트엔드로 JWT 토큰을 전달하기 위한 리다이렉션 (테스트 페이지)
        response.sendRedirect("/oauth2/success?token=" + token);
    }
    
    @Operation(summary = "OAuth2 로그인 성공", description = "토큰이 발급된 후 리다이렉트되는 페이지")
    @GetMapping("/success")
    public ResponseEntity<ApiResponse<Map<String, String>>> success(
            @Parameter(description = "발급된 JWT 토큰") @RequestParam String token) {
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(ApiResponse.success("소셜 로그인이 완료되었습니다.", response));
    }
    
    @Operation(summary = "OAuth2 로그인 테스트 페이지", description = "소셜 로그인을 테스트할 수 있는 페이지")
    @GetMapping("/test")
    public String testPage() {
        return "<html><head><title>OAuth2 로그인 테스트</title></head>"
                + "<body>"
                + "<h1>Google OAuth2 로그인 테스트</h1>"
                + "<a href='/oauth2/login/google' style='display:inline-block; background-color:#4285F4; color:white; padding:10px 20px; text-decoration:none; border-radius:5px; margin-top:20px;'>Google로 로그인</a>"
                + "</body></html>";
    }
} 