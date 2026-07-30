package com.example.bookkeeping.sale.mapper;

import com.example.bookkeeping.sale.entity.BizSaleItem;
import com.example.bookkeeping.sale.vo.SaleItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizSaleItemMapper {
    int insert(BizSaleItem item);

    List<SaleItemVO> selectVOBySaleRecordId(@Param("saleRecordId") Long saleRecordId);
}
