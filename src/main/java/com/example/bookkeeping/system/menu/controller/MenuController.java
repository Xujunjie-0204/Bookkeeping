package com.example.bookkeeping.system.menu.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.security.UserPrincipal;
import com.example.bookkeeping.system.menu.dto.SaveMenuRequest;
import com.example.bookkeeping.system.menu.service.MenuService;
import com.example.bookkeeping.system.menu.vo.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/system/menus")
@Tag(name = "菜单权限管理")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    @Operation(summary = "查询菜单树")
    public ApiResult<List<MenuVO>> listTree() {
        return ApiResult.success(menuService.listTree());
    }

    @GetMapping("/current")
    @Operation(summary = "查询当前登录用户菜单树")
    public ApiResult<List<MenuVO>> currentUserMenus(@AuthenticationPrincipal UserPrincipal principal) {
        return ApiResult.success(menuService.listCurrentUserMenus(principal.getId()));
    }

    @PostMapping
    @Operation(summary = "新增菜单权限")
    public ApiResult<MenuVO> create(@Valid @RequestBody SaveMenuRequest request) {
        return ApiResult.success(menuService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改菜单权限")
    public ApiResult<MenuVO> update(@PathVariable Long id, @Valid @RequestBody SaveMenuRequest request) {
        return ApiResult.success(menuService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单权限")
    public ApiResult<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ApiResult.success();
    }
}
