package com.example.bookkeeping.system.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "保存系统配置请求")
public class SaveConfigRequest {

    @NotBlank(message = "配置编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9_:.-]{2,80}$", message = "配置编码只能包含字母、数字、下划线、冒号、点和中横线")
    @Schema(description = "配置编码", required = true)
    private String configCode;

    @NotBlank(message = "配置名称不能为空")
    @Size(max = 80, message = "配置名称长度不能超过80位")
    @Schema(description = "配置名称", required = true)
    private String configName;

    @NotBlank(message = "配置JSON不能为空")
    @Schema(description = "配置JSON字符串", required = true)
    private String configValue;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    @Schema(description = "状态：1启用，0停用", required = true)
    private Integer status = 1;

    @Size(max = 500, message = "备注长度不能超过500位")
    @Schema(description = "备注")
    private String remark;
}
