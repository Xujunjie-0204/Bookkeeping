package com.example.bookkeeping.system.config.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "系统配置")
public class ConfigVO {

    private Long id;
    private String configCode;
    private String configName;
    private String configValue;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
