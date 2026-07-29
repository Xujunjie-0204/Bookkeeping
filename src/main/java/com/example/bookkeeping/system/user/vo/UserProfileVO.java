package com.example.bookkeeping.system.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "当前登录用户信息")
public class UserProfileVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "状态，1启用，0停用")
    private Integer status;

    @Schema(description = "上次登录时间")
    private LocalDateTime lastLoginAt;
}
