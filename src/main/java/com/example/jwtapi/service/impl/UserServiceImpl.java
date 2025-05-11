package com.example.jwtapi.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.exception.ResourceNotFoundException;
import com.example.jwtapi.mapper.UserMapper;
import com.example.jwtapi.model.User;
import com.example.jwtapi.security.JwtTokenProvider;
import com.example.jwtapi.service.UserService;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDTO signup(UserSignupVO userSignupVO) {
        // 이미 존재하는 사용자 확인
        if (userMapper.findByUsername(userSignupVO.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        // VO -> DTO 변환
        UserDTO userDTO = modelMapper.map(userSignupVO, UserDTO.class);
        userDTO.setPassword(passwordEncoder.encode(userSignupVO.getPassword()));
        userDTO.setCreatedAt(LocalDateTime.now());
        userDTO.setUpdatedAt(LocalDateTime.now());
        
        // DTO -> Entity 변환
        User userEntity = modelMapper.map(userDTO, User.class);
        
        // 저장
        userMapper.insertUser(userEntity);
        
        // ID 설정
        userDTO.setId(userEntity.getId());
        
        return userDTO;
    }

    @Override
    public String login(UserLoginVO userLoginVO) {
        // 인증 시도
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userLoginVO.getUsername(), userLoginVO.getPassword())
        );
        
        // 인증 성공 시 사용자 조회
        User user = userMapper.findByUsername(userLoginVO.getUsername());
        
        // JWT 토큰 생성
        return jwtTokenProvider.createToken(user.getUsername(), user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(String username, UserUpdateVO userUpdateVO) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        // 변경 사항 적용
        if (userUpdateVO.getPassword() != null && !userUpdateVO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userUpdateVO.getPassword()));
        }
        
        if (userUpdateVO.getEmail() != null) {
            user.setEmail(userUpdateVO.getEmail());
        }
        
        if (userUpdateVO.getName() != null) {
            user.setName(userUpdateVO.getName());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        
        // 업데이트
        userMapper.updateUser(user);
        
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void deleteUser(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        
        userMapper.deleteUser(user.getId());
    }
} 