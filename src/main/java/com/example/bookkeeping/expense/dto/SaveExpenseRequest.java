package com.example.bookkeeping.expense.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SaveExpenseRequest {
    @NotNull(message = "支出日期不能为空")
    private LocalDate expenseDate;

    @NotBlank(message = "支出类型不能为空")
    private String expenseType;

    @NotBlank(message = "支出名称不能为空")
    private String expenseName;

    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    private BigDecimal amount;

    private String expressCompany;
    private Integer shipmentCount;
    private String paymentMethod;
    private String voucherNo;
    private String remark;
}
