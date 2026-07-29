package com.example.bookkeeping.purchase.mapper;

import com.example.bookkeeping.purchase.entity.BizPurchase;
import com.example.bookkeeping.purchase.vo.PurchaseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BizPurchaseMapper {
    BizPurchase selectById(@Param("id") Long id);

    BizPurchase selectByPurchaseNo(@Param("purchaseNo") String purchaseNo);

    List<PurchaseVO> selectPage(@Param("keyword") String keyword, @Param("purchaseStatus") Integer purchaseStatus);

    int insert(BizPurchase purchase);

    int deleteById(@Param("id") Long id);
}
