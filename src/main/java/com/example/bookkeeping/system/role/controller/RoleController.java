package com.example.bookkeeping.system.role.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.system.role.dto.AssignRoleMenusRequest;
import com.example.bookkeeping.system.role.dto.SaveRoleRequest;
import com.example.bookkeeping.system.role.service.RoleService;
import com.example.bookkeeping.system.role.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/system/roles")
@Tag(name = "角色管理")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    @Operation(summary = "查询角色列表")
    public ApiResult<List<RoleVO>> list() {
        return ApiResult.success(roleService.list());
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public ApiResult<RoleVO> create(@Valid @RequestBody SaveRoleRequest request) {
        return ApiResult.success(roleService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改角色")
    public ApiResult<RoleVO> update(@PathVariable Long id, @Valid @RequestBody SaveRoleRequest request) {
        return ApiResult.success(roleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public ApiResult<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ApiResult.success();
    }

    @GetMapping("/{id}/menu-ids")
    @Operation(summary = "查询角色已分配菜单ID")
    public ApiResult<List<Long>> getMenuIds(@PathVariable Long id) {
        return ApiResult.success(roleService.getMenuIds(id));
    }

    @PutMapping("/{id}/menus")
    @Operation(summary = "分配角色菜单权限")
    public ApiResult<Void> assignMenus(@PathVariable Long id,
                                       @Valid @RequestBody AssignRoleMenusRequest request) {
        roleService.assignMenus(id, request);
        return ApiResult.success();
    }
}
