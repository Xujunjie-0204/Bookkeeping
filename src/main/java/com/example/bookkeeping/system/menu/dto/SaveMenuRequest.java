package com.example.bookkeeping.system.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "保存菜单请求")
public class SaveMenuRequest {

    @Schema(description = "父菜单ID，根节点传0")
    private Long parentId = 0L;

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50位")
    @Schema(description = "菜单名称", required = true)
    private String menuName;

    @NotBlank(message = "菜单类型不能为空")
    @Pattern(regexp = "M|C|F", message = "菜单类型只能为M、C或F")
    @Schema(description = "菜单类型：M目录，C菜单，F按钮", required = true)
    private String menuType;

    @Size(max = 120, message = "路由地址长度不能超过120位")
    @Schema(description = "路由地址")
    private String path;

    @Size(max = 120, message = "组件路径长度不能超过120位")
    @Schema(description = "组件路径")
    private String component;

    @Size(max = 100, message = "权限标识长度不能超过100位")
    @Schema(description = "权限标识")
    private String permissionCode;

    @Size(max = 50, message = "图标长度不能超过50位")
    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序号")
    private Integer sortOrder = 0;

    @NotNull(message = "是否显示不能为空")
    @Min(value = 0, message = "是否显示只能为0或1")
    @Max(value = 1, message = "是否显示只能为0或1")
    @Schema(description = "是否显示：1显示，0隐藏", required = true)
    private Integer visible = 1;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态只能为0或1")
    @Max(value = 1, message = "状态只能为0或1")
    @Schema(description = "状态：1启用，0停用", required = true)
    private Integer status = 1;

    @Size(max = 500, message = "备注长度不能超过500位")
    @Schema(description = "备注")
    private String remark;
}
