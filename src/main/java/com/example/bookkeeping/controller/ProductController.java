package com.example.bookkeeping.controller;

import com.example.bookkeeping.common.api.ApiResult;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.product.dto.ProductQueryRequest;
import com.example.bookkeeping.product.dto.SaveProductRequest;
import com.example.bookkeeping.product.service.ProductService;
import com.example.bookkeeping.product.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "分页查询商品列表")
    public ApiResult<PageResult<ProductVO>> page(@ModelAttribute ProductQueryRequest request) {
        return ApiResult.success(productService.page(request));
    }

    @PostMapping
    @Operation(summary = "新增商品")
    public ApiResult<ProductVO> create(@Valid @RequestBody SaveProductRequest request) {
        return ApiResult.success(productService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改商品")
    public ApiResult<ProductVO> update(@PathVariable Long id, @Valid @RequestBody SaveProductRequest request) {
        return ApiResult.success(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public ApiResult<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResult.success();
    }
}
