package com.example.jwtapi.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.User;
import com.example.jwtapi.security.JwtTokenProvider;
import com.example.jwtapi.service.OAuth2Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2ServiceImpl implements OAuth2Service {

    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    
    @Value("${oauth2.authorized-redirect-uri}")
    private String redirectUri;
    
    @Override
    public String processOAuth2Login(String code) {
        // 1. 코드를 사용하여 액세스 토큰 요청
        String accessToken = getAccessToken(code);
        
        // 2. 액세스 토큰으로 사용자 정보 가져오기
        Map<String, Object> userAttributes = getUserAttributes(accessToken);
        
        // 3. 사용자 정보로 회원가입/로그인 처리
        String googleId = (String) userAttributes.get("sub");
        String email = (String) userAttributes.get("email");
        String name = (String) userAttributes.get("name");
        String pictureUrl = (String) userAttributes.get("picture");
        
        // 이미 가입된 사용자인지 확인
        User user = userMapper.findByGoogleId(googleId);
        
        if (user == null) {
            // 이메일로 사용자를 찾아 Google 계정과 연결
            user = userMapper.findByEmail(email);
            
            if (user == null) {
                // 새 사용자 생성 및 저장
                user = User.builder()
                    .username(email)  // 이메일을 username으로 사용
                    .email(email)
                    .name(name)
                    .googleId(googleId)
                    .profileImage(pictureUrl)
                    .authProvider("GOOGLE")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                
                userMapper.insertUser(user);
            } else {
                // 기존 사용자에 Google 정보 추가
                user.setGoogleId(googleId);
                user.setProfileImage(pictureUrl);
                user.setAuthProvider("GOOGLE");
                user.setUpdatedAt(LocalDateTime.now());
                userMapper.updateUser(user);
            }
        }
        
        // 인증 정보 설정
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            user.getUsername(), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // JWT 토큰 발급
        return jwtTokenProvider.createToken(user.getUsername(), user.getId());
    }
    
    @Override
    public UserDTO getCurrentOAuth2User(String token) {
        String username = jwtTokenProvider.getUsername(token);
        User user = userMapper.findByUsername(username);
        
        if (user == null) {
            return null;
        }
        
        return modelMapper.map(user, UserDTO.class);
    }
    
    private String getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("code", code);
        parameters.add("redirect_uri", redirectUri);
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, headers);
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
            tokenUrl, 
            HttpMethod.POST, 
            requestEntity, 
            new ParameterizedTypeReference<Map<String, Object>>() {});
        
        Map<String, Object> responseBody = responseEntity.getBody();
        return (String) responseBody.get("access_token");
    }
    
    private Map<String, Object> getUserAttributes(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            userInfoUrl, 
            HttpMethod.GET, 
            entity, 
            new ParameterizedTypeReference<Map<String, Object>>() {});
        
        return response.getBody();
    }
} 