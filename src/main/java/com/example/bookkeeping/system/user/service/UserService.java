package com.example.bookkeeping.system.user.service;

import com.example.bookkeeping.system.user.dto.ChangePasswordRequest;
import com.example.bookkeeping.system.user.dto.LoginRequest;
import com.example.bookkeeping.system.user.vo.LoginResponse;
import com.example.bookkeeping.system.user.vo.UserProfileVO;

public interface UserService {

    LoginResponse login(LoginRequest request);

    UserProfileVO getProfile(Long userId);

    void changePassword(Long userId, ChangePasswordRequest request);
}
