package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.system.config.dto.SaveConfigRequest;
import com.example.bookkeeping.system.config.service.ConfigService;
import com.example.bookkeeping.system.config.vo.ConfigVO;
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
@RequestMapping("/api/system/configs")
@Tag(name = "系统配置管理")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    @Operation(summary = "查询系统配置列表")
    public ApiResult<List<ConfigVO>> list() {
        return ApiResult.success(configService.list());
    }

    @PostMapping
    @Operation(summary = "新增系统配置")
    public ApiResult<ConfigVO> create(@Valid @RequestBody SaveConfigRequest request) {
        return ApiResult.success(configService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改系统配置")
    public ApiResult<ConfigVO> update(@PathVariable Long id, @Valid @RequestBody SaveConfigRequest request) {
        return ApiResult.success(configService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除系统配置")
    public ApiResult<Void> delete(@PathVariable Long id) {
        configService.delete(id);
        return ApiResult.success();
    }
}
