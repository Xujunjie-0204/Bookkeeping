package com.example.bookkeeping.product.service;

import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.product.dto.ProductQueryRequest;
import com.example.bookkeeping.product.dto.SaveProductRequest;
import com.example.bookkeeping.product.vo.ProductVO;

public interface ProductService {

    PageResult<ProductVO> page(ProductQueryRequest request);

    ProductVO create(SaveProductRequest request);

    ProductVO update(Long id, SaveProductRequest request);

    void delete(Long id);
}
