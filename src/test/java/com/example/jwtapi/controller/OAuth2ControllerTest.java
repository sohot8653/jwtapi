package com.example.jwtapi.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.jwtapi.service.OAuth2Service;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OAuth2ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OAuth2Service oAuth2Service;
    
    @Test
    public void testGoogleLoginRedirect() throws Exception {
        mockMvc.perform(get("/oauth2/login/google"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testCallbackWithCode() throws Exception {
        // 모의 토큰 생성
        String mockToken = "mock_jwt_token";
        when(oAuth2Service.processOAuth2Login(anyString())).thenReturn(mockToken);
        
        mockMvc.perform(get("/oauth2/callback")
                .param("code", "test_code"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/oauth2/success?token=" + mockToken));
    }
    
    @Test
    public void testSuccessPage() throws Exception {
        String mockToken = "mock_jwt_token";
        
        mockMvc.perform(get("/oauth2/success")
                .param("token", mockToken))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());
    }
    
    @Test
    public void testTestPage() throws Exception {
        mockMvc.perform(get("/oauth2/test"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());
    }
} 