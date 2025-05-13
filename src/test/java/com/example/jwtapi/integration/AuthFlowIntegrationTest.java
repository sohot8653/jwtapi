package com.example.jwtapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.example.jwtapi.AbstractTestBase;
import com.example.jwtapi.TestUtil;
import com.example.jwtapi.dto.ApiResponse;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.fasterxml.jackson.core.type.TypeReference;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthFlowIntegrationTest extends AbstractTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    @DisplayName("인증 통합 플로우 테스트")
    @Sql(statements = {
        "DELETE FROM todos", 
        "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testAuthFlow() throws Exception {
        // 2단계: 회원가입 통합 테스트
        UserSignupVO userSignupVO = TestUtil.createTestUserSignupVO();
        
        // 디버깅을 위한 출력
        System.out.println("회원가입 요청 데이터: " + objectMapper.writeValueAsString(userSignupVO));
        
        ResultActions signupResult = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupVO)));
        
        // 디버깅을 위해 응답 본문 출력
        MvcResult signupMvcResult = signupResult.andReturn();
        if (signupMvcResult.getResponse().getStatus() != 200) {
            System.out.println("회원가입 응답 상태: " + signupMvcResult.getResponse().getStatus());
            System.out.println("회원가입 응답 본문: " + signupMvcResult.getResponse().getContentAsString());
        }
        
        signupResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.username").value(userSignupVO.getUsername()))
              .andExpect(jsonPath("$.data.email").value(userSignupVO.getEmail()))
              .andExpect(jsonPath("$.data.name").value(userSignupVO.getName()));
        
        // 3단계: 로그인 통합 테스트
        UserLoginVO userLoginVO = TestUtil.createTestUserLoginVO();
        
        MvcResult loginResult = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().exists("Authorization"))
                .andReturn();
        
        // JWT 토큰 저장
        String jwtToken = loginResult.getResponse().getHeader("Authorization").replace("Bearer ", "");
        
        // 4단계: JWT 인증 통합 테스트 - 내 정보 조회
        ResultActions authResult = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        authResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.username").value(userSignupVO.getUsername()))
              .andExpect(jsonPath("$.data.email").value(userSignupVO.getEmail()))
              .andExpect(jsonPath("$.data.name").value(userSignupVO.getName()));
        
        // 5단계: JWT 없이 접근 시 401 응답 테스트
        ResultActions unauthorizedResult = mockMvc.perform(get("/users/me")
                .contentType(MediaType.APPLICATION_JSON));
        
        unauthorizedResult.andExpect(status().isUnauthorized())
              .andExpect(jsonPath("$.success").value(false))
              .andExpect(jsonPath("$.message").isNotEmpty());
        
        // 6단계: 잘못된 JWT로 접근 시 401 응답 테스트
        ResultActions invalidJwtResult = mockMvc.perform(get("/users/me")
                .header("Authorization", "Bearer invalid.jwt.token")
                .contentType(MediaType.APPLICATION_JSON));
        
        invalidJwtResult.andExpect(status().isUnauthorized())
              .andExpect(jsonPath("$.success").value(false))
              .andExpect(jsonPath("$.message").isNotEmpty());
    }
} 