package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.product.dto.SaveProductTypeRequest;
import com.example.bookkeeping.product.service.ProductTypeService;
import com.example.bookkeeping.product.vo.ProductTypeVO;
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
@RequestMapping("/api/product-types")
@Tag(name = "商品类型管理")
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    @Operation(summary = "查询商品类型树")
    public ApiResult<List<ProductTypeVO>> listTree() {
        return ApiResult.success(productTypeService.listTree());
    }

    @PostMapping
    @Operation(summary = "新增商品类型")
    public ApiResult<ProductTypeVO> create(@Valid @RequestBody SaveProductTypeRequest request) {
        return ApiResult.success(productTypeService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改商品类型")
    public ApiResult<ProductTypeVO> update(@PathVariable Long id,
                                           @Valid @RequestBody SaveProductTypeRequest request) {
        return ApiResult.success(productTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品类型")
    public ApiResult<Void> delete(@PathVariable Long id) {
        productTypeService.delete(id);
        return ApiResult.success();
    }
}
