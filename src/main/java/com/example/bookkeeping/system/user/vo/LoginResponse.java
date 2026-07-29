package com.example.bookkeeping.system.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应")
public class LoginResponse {

    @Schema(description = "JWT Token")
    private String token;

    @Schema(description = "Token 类型")
    private String tokenType;

    @Schema(description = "有效期秒数")
    private long expiresIn;

    @Schema(description = "用户信息")
    private UserProfileVO user;
}
