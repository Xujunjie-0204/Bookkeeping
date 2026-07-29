package com.example.bookkeeping.system.user.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.security.UserPrincipal;
import com.example.bookkeeping.system.user.dto.ChangePasswordRequest;
import com.example.bookkeeping.system.user.dto.LoginRequest;
import com.example.bookkeeping.system.user.service.UserService;
import com.example.bookkeeping.system.user.vo.LoginResponse;
import com.example.bookkeeping.system.user.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "登录和鉴权")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户名密码登录")
    public ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResult.success(userService.login(request));
    }

    @GetMapping("/profile")
    @Operation(summary = "获取当前登录用户")
    public ApiResult<UserProfileVO> profile(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResult.success(userService.getProfile(principal.getId()));
    }

    @PostMapping("/logout")
    @Operation(summary = "退出登录")
    public ApiResult<Void> logout() {
        return ApiResult.success();
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public ApiResult<Void> changePassword(@AuthenticationPrincipal UserPrincipal principal,
                                          @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getId(), request);
        return ApiResult.success();
    }
}
