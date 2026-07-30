package com.example.bookkeeping.sale.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class SaleQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private Integer paymentStatus;
    private Integer shipmentStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
