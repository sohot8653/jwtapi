package com.example.jwtapi.service;

import com.example.jwtapi.dto.UserDTO;
import com.example.jwtapi.vo.user.UserLoginVO;
import com.example.jwtapi.vo.user.UserSignupVO;
import com.example.jwtapi.vo.user.UserUpdateVO;

public interface UserService {
    UserDTO signup(UserSignupVO userSignupVO);
    String login(UserLoginVO userLoginVO);
    UserDTO getCurrentUser(String username);
    UserDTO updateUser(String username, UserUpdateVO userUpdateVO);
    void deleteUser(String username);
} 