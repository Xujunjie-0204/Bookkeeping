package com.example.bookkeeping.purchase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "保存采购单请求")
public class SavePurchaseRequest {
    @Size(max = 50, message = "采购单号长度不能超过50位")
    private String purchaseNo;

    @NotBlank(message = "采购平台不能为空")
    @Size(max = 30, message = "采购平台长度不能超过30位")
    private String platform;

    @Size(max = 80, message = "平台订单号长度不能超过80位")
    private String platformOrderNo;

    @Size(max = 100, message = "供应商长度不能超过100位")
    private String supplierName;

    @Size(max = 100, message = "卖家账号长度不能超过100位")
    private String sellerAccount;

    @NotNull(message = "采购日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    @DecimalMin(value = "0.00", message = "采购运费不能小于0")
    private BigDecimal freightAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "优惠金额不能小于0")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.00", message = "其他费用不能小于0")
    private BigDecimal otherAmount = BigDecimal.ZERO;

    @Size(max = 50, message = "付款方式长度不能超过50位")
    private String paymentMethod;

    @Size(max = 500, message = "备注长度不能超过500位")
    private String remark;

    @Valid
    @NotEmpty(message = "采购明细不能为空")
    private List<SavePurchaseItemRequest> items = new ArrayList<SavePurchaseItemRequest>();
}
