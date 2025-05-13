package com.example.jwtapi.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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
import com.example.jwtapi.dto.TodoDTO;
import com.example.jwtapi.vo.todo.TodoCreateVO;
import com.example.jwtapi.vo.todo.TodoUpdateVO;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.fasterxml.jackson.core.type.TypeReference;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TodoFlowIntegrationTest extends AbstractTestBase {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    @DisplayName("TODO 통합 플로우 테스트")
    @Sql(statements = {
        "DELETE FROM todos", 
        "DELETE FROM users"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testTodoFlow() throws Exception {
        // 1단계: 회원가입
        UserSignupVO userSignupVO = TestUtil.createTestUserSignupVO();
        
        ResultActions signupResult = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userSignupVO)));
        
        signupResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.username").value(userSignupVO.getUsername()));
        
        // 2단계: 로그인하여 JWT 토큰 획득
        UserLoginVO userLoginVO = TestUtil.createTestUserLoginVO();
        
        MvcResult loginResult = mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(header().exists("Authorization"))
                .andReturn();
        
        String jwtToken = loginResult.getResponse().getHeader("Authorization").replace("Bearer ", "");
        
        // 3단계: TODO 생성
        TodoCreateVO todoCreateVO = TestUtil.createTestTodoCreateVO();
        
        MvcResult createResult = mockMvc.perform(post("/todos")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoCreateVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value(todoCreateVO.getTitle()))
                .andReturn();
        
        // 생성된 TODO의 ID 추출
        String responseBody = createResult.getResponse().getContentAsString();
        TodoDTO createdTodo = objectMapper.readValue(
            objectMapper.readTree(responseBody).get("data").toString(),
            TodoDTO.class
        );
        Long todoId = createdTodo.getId();
        
        // 4단계: TODO 목록 조회
        ResultActions listResult = mockMvc.perform(get("/todos")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        listResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data[0].id").value(todoId))
              .andExpect(jsonPath("$.data[0].title").value(todoCreateVO.getTitle()));
        
        // 5단계: TODO 상세 조회
        ResultActions getResult = mockMvc.perform(get("/todos/" + todoId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        getResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.id").value(todoId))
              .andExpect(jsonPath("$.data.title").value(todoCreateVO.getTitle()));
        
        // 6단계: TODO 수정
        TodoUpdateVO todoUpdateVO = TestUtil.createTestTodoUpdateVO();
        
        ResultActions updateResult = mockMvc.perform(put("/todos/" + todoId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoUpdateVO)));
        
        updateResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.title").value(todoUpdateVO.getTitle()))
              .andExpect(jsonPath("$.data.content").value(todoUpdateVO.getContent()))
              .andExpect(jsonPath("$.data.completed").value(todoUpdateVO.getCompleted()));
        
        // 7단계: 수정된 TODO 확인
        ResultActions verifyUpdateResult = mockMvc.perform(get("/todos/" + todoId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        verifyUpdateResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true))
              .andExpect(jsonPath("$.data.title").value(todoUpdateVO.getTitle()))
              .andExpect(jsonPath("$.data.content").value(todoUpdateVO.getContent()))
              .andExpect(jsonPath("$.data.completed").value(todoUpdateVO.getCompleted()));
        
        // 8단계: TODO 삭제
        ResultActions deleteResult = mockMvc.perform(delete("/todos/" + todoId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        deleteResult.andExpect(status().isOk())
              .andExpect(jsonPath("$.success").value(true));
        
        // 9단계: 삭제된 TODO 확인
        ResultActions verifyDeleteResult = mockMvc.perform(get("/todos/" + todoId)
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON));
        
        verifyDeleteResult.andExpect(status().isNotFound());
    }
} 