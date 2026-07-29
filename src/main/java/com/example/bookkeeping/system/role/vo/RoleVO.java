package com.example.bookkeeping.system.role.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "角色信息")
public class RoleVO {

    private Long id;
    private String roleCode;
    private String roleName;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
