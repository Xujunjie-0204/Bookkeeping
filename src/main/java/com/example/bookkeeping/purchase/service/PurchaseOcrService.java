package com.example.bookkeeping.purchase.service;

import com.example.bookkeeping.purchase.vo.PurchaseOcrVO;
import org.springframework.web.multipart.MultipartFile;

public interface PurchaseOcrService {
    PurchaseOcrVO recognize(MultipartFile file);
}
