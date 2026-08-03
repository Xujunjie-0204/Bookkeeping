package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.purchase.dto.PurchaseQueryRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseRequest;
import com.example.bookkeeping.purchase.dto.UpdatePurchaseRequest;
import com.example.bookkeeping.purchase.service.PurchaseOcrService;
import com.example.bookkeeping.purchase.service.PurchaseService;
import com.example.bookkeeping.purchase.vo.PurchaseItemVO;
import com.example.bookkeeping.purchase.vo.PurchaseOcrVO;
import com.example.bookkeeping.purchase.vo.PurchaseSummaryVO;
import com.example.bookkeeping.purchase.vo.PurchaseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/purchases")
@Tag(name = "采购进货管理")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final PurchaseOcrService purchaseOcrService;

    public PurchaseController(PurchaseService purchaseService, PurchaseOcrService purchaseOcrService) {
        this.purchaseService = purchaseService;
        this.purchaseOcrService = purchaseOcrService;
    }

    @GetMapping
    @Operation(summary = "分页查询采购单")
    public ApiResult<PageResult<PurchaseVO>> page(@ModelAttribute PurchaseQueryRequest request) {
        return ApiResult.success(purchaseService.page(request));
    }

    @GetMapping("/summary")
    @Operation(summary = "查询采购统计")
    public ApiResult<PurchaseSummaryVO> summary(@ModelAttribute PurchaseQueryRequest request) {
        return ApiResult.success(purchaseService.summary(request));
    }

    @PostMapping
    @Operation(summary = "新增采购进货单并入库")
    public ApiResult<PurchaseVO> create(@Valid @RequestBody SavePurchaseRequest request) {
        return ApiResult.success(purchaseService.create(request));
    }

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "OCR识别采购订单截图")
    public ApiResult<PurchaseOcrVO> ocr(@RequestParam("file") MultipartFile file) {
        return ApiResult.success(purchaseOcrService.recognize(file));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改采购进货单基础信息")
    public ApiResult<PurchaseVO> update(@PathVariable Long id, @Valid @RequestBody UpdatePurchaseRequest request) {
        return ApiResult.success(purchaseService.update(id, request));
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "查询采购单商品明细")
    public ApiResult<List<PurchaseItemVO>> items(@PathVariable Long id) {
        return ApiResult.success(purchaseService.items(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除采购单")
    public ApiResult<Void> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ApiResult.success();
    }
}
