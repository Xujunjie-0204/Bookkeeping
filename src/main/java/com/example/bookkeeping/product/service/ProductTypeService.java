package com.example.bookkeeping.product.service;

import com.example.bookkeeping.product.dto.SaveProductTypeRequest;
import com.example.bookkeeping.product.vo.ProductTypeVO;

import java.util.List;

public interface ProductTypeService {

    List<ProductTypeVO> listTree();

    ProductTypeVO create(SaveProductTypeRequest request);

    ProductTypeVO update(Long id, SaveProductTypeRequest request);

    void delete(Long id);
}
