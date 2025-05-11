package com.example.jwtapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jwtapi.TestUtil;
import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.exception.ResourceNotFoundException;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.User;
import com.example.jwtapi.security.JwtTokenProvider;
import com.example.jwtapi.service.impl.UserServiceImpl;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;
    
    @Spy
    private ModelMapper modelMapper = new ModelMapper();
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    private User testUser;
    private UserSignupVO testUserSignupVO;
    private UserLoginVO testUserLoginVO;
    private UserUpdateVO testUserUpdateVO;
    
    @BeforeEach
    void setUp() {
        testUser = TestUtil.createTestUser();
        testUserSignupVO = TestUtil.createTestUserSignupVO();
        testUserLoginVO = TestUtil.createTestUserLoginVO();
        testUserUpdateVO = TestUtil.createTestUserUpdateVO();
    }
    
    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignupSuccess() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        
        // ID 설정을 위한 모킹
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return null;
        }).when(userMapper).insertUser(any(User.class));
        
        // When
        UserDTO result = userService.signup(testUserSignupVO);
        
        // Then
        assertNotNull(result);
        assertEquals(testUserSignupVO.getUsername(), result.getUsername());
        assertEquals(testUserSignupVO.getEmail(), result.getEmail());
        assertEquals(testUserSignupVO.getName(), result.getName());
        assertEquals(1L, result.getId());
        verify(userMapper, times(1)).insertUser(any(User.class));
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 사용자")
    void testSignupFailUserExists() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        
        // When & Then
        assertThrows(RuntimeException.class, () -> userService.signup(testUserSignupVO));
    }
    
    @Test
    @DisplayName("로그인 성공 테스트")
    void testLoginSuccess() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(jwtTokenProvider.createToken(anyString(), anyLong())).thenReturn("test.jwt.token");
        
        // When
        String token = userService.login(testUserLoginVO);
        
        // Then
        assertNotNull(token);
        assertEquals("test.jwt.token", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
    
    @Test
    @DisplayName("현재 사용자 조회 테스트")
    void testGetCurrentUser() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        
        // When
        UserDTO result = userService.getCurrentUser("testuser");
        
        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getName(), result.getName());
    }
    
    @Test
    @DisplayName("현재 사용자 조회 실패 - 사용자 없음")
    void testGetCurrentUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getCurrentUser("unknownuser"));
    }
    
    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    void testUpdateUser() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        doNothing().when(userMapper).updateUser(any(User.class));
        
        // When
        UserDTO result = userService.updateUser("testuser", testUserUpdateVO);
        
        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUserUpdateVO.getEmail(), result.getEmail());
        assertEquals(testUserUpdateVO.getName(), result.getName());
        verify(userMapper, times(1)).updateUser(any(User.class));
    }
    
    @Test
    @DisplayName("사용자 정보 업데이트 실패 - 사용자 없음")
    void testUpdateUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser("unknownuser", testUserUpdateVO));
    }
    
    @Test
    @DisplayName("사용자 정보 업데이트 - 비밀번호 포함")
    void testUpdateUserWithPassword() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        doNothing().when(userMapper).updateUser(any(User.class));
        
        // 비밀번호 포함한 업데이트 정보
        UserUpdateVO updateWithPassword = new UserUpdateVO();
        updateWithPassword.setEmail(testUserUpdateVO.getEmail());
        updateWithPassword.setName(testUserUpdateVO.getName());
        updateWithPassword.setPassword("newPassword");
        
        // When
        UserDTO result = userService.updateUser("testuser", updateWithPassword);
        
        // Then
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userMapper, times(1)).updateUser(any(User.class));
    }
    
    @Test
    @DisplayName("사용자 삭제 테스트")
    void testDeleteUser() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(testUser);
        doNothing().when(userMapper).deleteUser(anyLong());
        
        // When
        userService.deleteUser("testuser");
        
        // Then
        verify(userMapper, times(1)).deleteUser(testUser.getId());
    }
    
    @Test
    @DisplayName("사용자 삭제 실패 - 사용자 없음")
    void testDeleteUserNotFound() {
        // Given
        when(userMapper.findByUsername(anyString())).thenReturn(null);
        
        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("unknownuser"));
    }
} 