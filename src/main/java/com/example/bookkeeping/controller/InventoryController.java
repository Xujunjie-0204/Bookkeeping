package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.inventory.dto.InventoryQueryRequest;
import com.example.bookkeeping.inventory.service.InventoryService;
import com.example.bookkeeping.inventory.vo.InventorySummaryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "库存管理")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @Operation(summary = "分页查询当前库存")
    public ApiResult<PageResult<InventorySummaryVO>> page(@ModelAttribute InventoryQueryRequest request) {
        return ApiResult.success(inventoryService.page(request));
    }
}
