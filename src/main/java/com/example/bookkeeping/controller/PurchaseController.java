package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.purchase.dto.PurchaseQueryRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseRequest;
import com.example.bookkeeping.purchase.service.PurchaseService;
import com.example.bookkeeping.purchase.vo.PurchaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/purchases")
@Tag(name = "采购进货管理")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    @Operation(summary = "分页查询采购单")
    public ApiResult<PageResult<PurchaseVO>> page(@ModelAttribute PurchaseQueryRequest request) {
        return ApiResult.success(purchaseService.page(request));
    }

    @PostMapping
    @Operation(summary = "新增采购进货单并入库")
    public ApiResult<PurchaseVO> create(@Valid @RequestBody SavePurchaseRequest request) {
        return ApiResult.success(purchaseService.create(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除采购单")
    public ApiResult<Void> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ApiResult.success();
    }
}
