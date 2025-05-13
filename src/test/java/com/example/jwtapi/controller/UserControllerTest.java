package com.example.jwtapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import com.example.jwtapi.AbstractTestBase;
import com.example.jwtapi.TestUtil;
import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.service.UserService;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

class UserControllerTest extends AbstractTestBase {

    @MockBean
    private UserService userService;
    
    private UserDTO testUserDTO;
    private UserSignupVO testUserSignupVO;
    private UserLoginVO testUserLoginVO;
    private UserUpdateVO testUserUpdateVO;
    
    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setUsername("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setName("Test User");
        
        testUserSignupVO = TestUtil.createTestUserSignupVO();
        testUserLoginVO = TestUtil.createTestUserLoginVO();
        testUserUpdateVO = TestUtil.createTestUserUpdateVO();
    }
    
    @Test
    @DisplayName("회원가입 API 테스트")
    void testSignup() throws Exception {
        // Given
        when(userService.signup(any(UserSignupVO.class))).thenReturn(testUserDTO);
        
        // When
        ResultActions result = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserSignupVO)));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.id").value(testUserDTO.getId()))
              .andExpect(jsonPath("$.data.username").value(testUserDTO.getUsername()));
    }
    
    @Test
    @DisplayName("로그인 API 테스트")
    void testLogin() throws Exception {
        // Given
        String token = "test.jwt.token";
        when(userService.login(any(UserLoginVO.class))).thenReturn(token);
        
        // When
        ResultActions result = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserLoginVO)));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(header().string("Authorization", "Bearer " + token));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("내 정보 조회 API 테스트")
    void testGetCurrentUser() throws Exception {
        // Given
        when(userService.getCurrentUser(anyString())).thenReturn(testUserDTO);
        
        // When
        ResultActions result = mockMvc.perform(get("/users/me")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.id").value(testUserDTO.getId()))
              .andExpect(jsonPath("$.data.username").value(testUserDTO.getUsername()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("내 정보 수정 API 테스트")
    void testUpdateUser() throws Exception {
        // Given
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setUsername("testuser");
        updatedUserDTO.setEmail(testUserUpdateVO.getEmail());
        updatedUserDTO.setName(testUserUpdateVO.getName());
        
        when(userService.updateUser(anyString(), any(UserUpdateVO.class))).thenReturn(updatedUserDTO);
        
        // When
        ResultActions result = mockMvc.perform(put("/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserUpdateVO)));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.email").value(testUserUpdateVO.getEmail()))
              .andExpect(jsonPath("$.data.name").value(testUserUpdateVO.getName()));
    }
    
    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("회원 탈퇴 API 테스트")
    void testDeleteUser() throws Exception {
        // When
        ResultActions result = mockMvc.perform(delete("/users/me")
                .contentType(MediaType.APPLICATION_JSON));
        
        // Then
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true));
    }
} 