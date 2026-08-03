package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.sale.dto.SaleQueryRequest;
import com.example.bookkeeping.sale.dto.SaveSaleRequest;
import com.example.bookkeeping.sale.dto.UpdateSaleRequest;
import com.example.bookkeeping.sale.service.SaleOcrService;
import com.example.bookkeeping.sale.service.SaleService;
import com.example.bookkeeping.sale.vo.SaleItemVO;
import com.example.bookkeeping.sale.vo.SaleOcrVO;
import com.example.bookkeeping.sale.vo.SaleStockVO;
import com.example.bookkeeping.sale.vo.SaleSummaryVO;
import com.example.bookkeeping.sale.vo.SaleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/sales")
@Tag(name = "销售管理")
public class SaleController {
    private final SaleService saleService;
    private final SaleOcrService saleOcrService;

    public SaleController(SaleService saleService, SaleOcrService saleOcrService) {
        this.saleService = saleService;
        this.saleOcrService = saleOcrService;
    }

    @GetMapping
    @Operation(summary = "分页查询销售订单")
    public ApiResult<PageResult<SaleVO>> page(@ModelAttribute SaleQueryRequest request) {
        return ApiResult.success(saleService.page(request));
    }

    @GetMapping("/summary")
    @Operation(summary = "查询销售统计")
    public ApiResult<SaleSummaryVO> summary(@ModelAttribute SaleQueryRequest request) {
        return ApiResult.success(saleService.summary(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询销售订单详情")
    public ApiResult<SaleVO> detail(@PathVariable Long id) {
        return ApiResult.success(saleService.detail(id));
    }

    @GetMapping("/available-stock")
    @Operation(summary = "查询可销售库存批次")
    public ApiResult<List<SaleStockVO>> availableStock(@RequestParam(required = false) String keyword,
                                                       @RequestParam(required = false) List<Long> productTypeIds) {
        return ApiResult.success(saleService.availableStock(keyword, productTypeIds));
    }

    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "OCR识别销售订单截图")
    public ApiResult<SaleOcrVO> ocr(@RequestParam("file") MultipartFile file) {
        return ApiResult.success(saleOcrService.recognize(file));
    }

    @PostMapping
    @Operation(summary = "新增销售订单并出库")
    public ApiResult<SaleVO> create(@Valid @RequestBody SaveSaleRequest request) {
        return ApiResult.success(saleService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改销售订单普通信息")
    public ApiResult<SaleVO> update(@PathVariable Long id, @Valid @RequestBody UpdateSaleRequest request) {
        return ApiResult.success(saleService.update(id, request));
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "查询销售订单明细")
    public ApiResult<List<SaleItemVO>> items(@PathVariable Long id) {
        return ApiResult.success(saleService.items(id));
    }
}
