package com.example.bookkeeping.system.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "分配角色菜单权限请求")
public class AssignRoleMenusRequest {

    @NotNull(message = "菜单ID列表不能为空")
    @Schema(description = "菜单ID列表", required = true)
    private List<Long> menuIds = new ArrayList<Long>();
}
