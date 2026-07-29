package com.example.bookkeeping.product.mapper;

import com.example.bookkeeping.product.entity.BizProductType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizProductTypeMapper {

    BizProductType selectById(@Param("id") Long id);

    BizProductType selectByTypeCode(@Param("typeCode") String typeCode);

    List<BizProductType> selectAll();

    int countChildren(@Param("parentId") Long parentId);

    int insert(BizProductType productType);

    int update(BizProductType productType);

    int deleteById(@Param("id") Long id);
}
