package com.example.bookkeeping.system.config.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysConfig {

    private Long id;
    private String configCode;
    private String configName;
    private String configValue;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}
