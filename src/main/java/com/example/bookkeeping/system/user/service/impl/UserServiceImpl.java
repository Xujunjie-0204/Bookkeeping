package com.example.bookkeeping.system.user.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.security.JwtProperties;
import com.example.bookkeeping.security.JwtTokenProvider;
import com.example.bookkeeping.security.UserPrincipal;
import com.example.bookkeeping.system.user.dto.ChangePasswordRequest;
import com.example.bookkeeping.system.user.dto.LoginRequest;
import com.example.bookkeeping.system.user.dto.UpdateProfileRequest;
import com.example.bookkeeping.system.user.entity.SysUser;
import com.example.bookkeeping.system.user.mapper.SysUserMapper;
import com.example.bookkeeping.system.user.service.UserService;
import com.example.bookkeeping.system.user.vo.LoginResponse;
import com.example.bookkeeping.system.user.vo.UserProfileVO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public UserServiceImpl(SysUserMapper sysUserMapper,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           JwtProperties jwtProperties) {
        this.sysUserMapper = sysUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserMapper.selectByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus().intValue() != 1) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账号已停用");
        }
        LocalDateTime now = LocalDateTime.now();
        sysUserMapper.updateLastLoginAt(user.getId(), now);
        user.setLastLoginAt(now);

        UserPrincipal principal = new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getNickname(),
                user.getStatus()
        );
        LoginResponse response = new LoginResponse();
        response.setToken(jwtTokenProvider.createToken(principal));
        response.setTokenType("Bearer");
        response.setExpiresIn(jwtProperties.getExpiresIn());
        response.setUser(toProfile(user));
        return response;
    }

    @Override
    public UserProfileVO getProfile(Long userId) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return toProfile(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO updateProfile(Long userId, UpdateProfileRequest request) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        String phone = request.getPhone() == null || request.getPhone().trim().isEmpty()
                ? null
                : request.getPhone().trim();
        sysUserMapper.updateProfile(userId, request.getNickname().trim(), phone);
        return getProfile(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, ChangePasswordRequest request) {
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "原密码错误");
        }
        sysUserMapper.updatePassword(userId, passwordEncoder.encode(request.getNewPassword()));
    }

    private UserProfileVO toProfile(SysUser user) {
        UserProfileVO vo = new UserProfileVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setPhone(user.getPhone());
        vo.setStatus(user.getStatus());
        vo.setLastLoginAt(user.getLastLoginAt());
        return vo;
    }
}
