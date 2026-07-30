package com.example.bookkeeping.expense.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ExpenseQueryRequest {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String keyword;
    private String expenseType;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
