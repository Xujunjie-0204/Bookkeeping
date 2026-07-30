package com.example.bookkeeping.expense.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ExpenseVO {
    private Long id;
    private String expenseNo;
    private LocalDate expenseDate;
    private String expenseType;
    private String expenseName;
    private BigDecimal amount;
    private String expressCompany;
    private Integer shipmentCount;
    private Long relatedSaleId;
    private String relatedOrderNo;
    private Integer independentFlag;
    private String paymentMethod;
    private String voucherNo;
    private String remark;
    private LocalDateTime createdAt;
}
