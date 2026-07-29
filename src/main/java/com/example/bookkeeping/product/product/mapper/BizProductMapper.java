package com.example.bookkeeping.product.product.mapper;

import com.example.bookkeeping.product.product.entity.BizProduct;
import com.example.bookkeeping.product.product.vo.ProductVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizProductMapper {

    BizProduct selectById(@Param("id") Long id);

    BizProduct selectByProductCode(@Param("productCode") String productCode);

    List<ProductVO> selectPage(@Param("keyword") String keyword,
                               @Param("productTypeId") Long productTypeId,
                               @Param("status") Integer status);

    int insert(BizProduct product);

    int update(BizProduct product);

    int deleteById(@Param("id") Long id);
}
