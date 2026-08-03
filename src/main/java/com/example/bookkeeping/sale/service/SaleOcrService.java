package com.example.bookkeeping.sale.service;

import com.example.bookkeeping.sale.vo.SaleOcrVO;
import org.springframework.web.multipart.MultipartFile;

public interface SaleOcrService {
    SaleOcrVO recognize(MultipartFile file);
}
