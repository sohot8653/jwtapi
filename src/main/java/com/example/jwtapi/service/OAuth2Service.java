package com.example.jwtapi.service;

import com.example.jwtapi.dto.UserDTO;
 
public interface OAuth2Service {
    String processOAuth2Login(String code);
    UserDTO getCurrentOAuth2User(String token);
} 