package com.example.jwtapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.User;
import com.example.jwtapi.security.JwtTokenProvider;
import com.example.jwtapi.service.impl.OAuth2ServiceImpl;

public class OAuth2ServiceTest {

    @Mock
    private UserMapper userMapper;
    
    @Mock
    private ModelMapper modelMapper;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private RestTemplate restTemplate;
    
    @InjectMocks
    private OAuth2ServiceImpl oauth2Service;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // 필요한 필드값 설정
        ReflectionTestUtils.setField(oauth2Service, "clientId", "test-client-id");
        ReflectionTestUtils.setField(oauth2Service, "clientSecret", "test-client-secret");
        ReflectionTestUtils.setField(oauth2Service, "redirectUri", "http://localhost:8080/oauth2/callback");
    }
    
    @Test
    public void testGetCurrentOAuth2User() {
        // given
        String token = "test-token";
        String username = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail(username);
        user.setName("Test User");
        user.setGoogleId("google-123");
        
        UserDTO expectedUserDTO = new UserDTO();
        expectedUserDTO.setId(1L);
        expectedUserDTO.setUsername(username);
        
        // when
        when(jwtTokenProvider.getUsername(token)).thenReturn(username);
        when(userMapper.findByUsername(username)).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(expectedUserDTO);
        
        // then
        UserDTO result = oauth2Service.getCurrentOAuth2User(token);
        
        // verify
        assertNotNull(result);
        assertEquals(expectedUserDTO.getId(), result.getId());
        assertEquals(expectedUserDTO.getUsername(), result.getUsername());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testProcessOAuth2Login_NewUser() throws Exception {
        // given
        String code = "test-code";
        String accessToken = "test-access-token";
        String username = "test@example.com";
        
        // Mock API 응답값
        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", accessToken);
        
        Map<String, Object> userInfoResponse = new HashMap<>();
        userInfoResponse.put("sub", "google-123");
        userInfoResponse.put("email", username);
        userInfoResponse.put("name", "Test User");
        userInfoResponse.put("picture", "https://example.com/profile.jpg");
        
        // API 호출 모의
        ResponseEntity<Map<String, Object>> tokenResponseEntity = new ResponseEntity<>(tokenResponse, HttpStatus.OK);
        ResponseEntity<Map<String, Object>> userInfoResponseEntity = new ResponseEntity<>(userInfoResponse, HttpStatus.OK);
        
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
            .thenReturn(tokenResponseEntity, userInfoResponseEntity);
        
        // DB 조회 결과 모의
        when(userMapper.findByGoogleId(anyString())).thenReturn(null);
        when(userMapper.findByEmail(anyString())).thenReturn(null);
        
        // 새 사용자 저장 모의
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setEmail(username);
        savedUser.setName("Test User");
        savedUser.setGoogleId("google-123");
        savedUser.setProfileImage("https://example.com/profile.jpg");
        savedUser.setAuthProvider("GOOGLE");
        
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userMapper).insertUser(any(User.class));
        
        // JWT 토큰 생성 모의
        when(jwtTokenProvider.createToken(anyString(), any(Long.class))).thenReturn("mocked-jwt-token");
        
        // when
        String result = oauth2Service.processOAuth2Login(code);
        
        // then
        assertNotNull(result);
        assertEquals("mocked-jwt-token", result);
    }
} 