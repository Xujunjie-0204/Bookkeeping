package com.example.bookkeeping.system.role.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysRole {

    private Long id;
    private String roleCode;
    private String roleName;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
