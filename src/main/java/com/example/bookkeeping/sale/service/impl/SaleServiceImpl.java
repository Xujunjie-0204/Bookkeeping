package com.example.bookkeeping.sale.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.inventory.entity.BizInventoryBatch;
import com.example.bookkeeping.inventory.entity.BizInventoryLog;
import com.example.bookkeeping.inventory.mapper.BizInventoryBatchMapper;
import com.example.bookkeeping.inventory.mapper.BizInventoryLogMapper;
import com.example.bookkeeping.purchase.mapper.BizPurchaseMapper;
import com.example.bookkeeping.sale.dto.SaleQueryRequest;
import com.example.bookkeeping.sale.dto.SaveSaleItemRequest;
import com.example.bookkeeping.sale.dto.SaveSaleRequest;
import com.example.bookkeeping.sale.entity.BizSaleItem;
import com.example.bookkeeping.sale.entity.BizSaleRecord;
import com.example.bookkeeping.sale.mapper.BizSaleItemMapper;
import com.example.bookkeeping.sale.mapper.BizSaleRecordMapper;
import com.example.bookkeeping.sale.service.SaleService;
import com.example.bookkeeping.sale.vo.SaleItemVO;
import com.example.bookkeeping.sale.vo.SaleStockVO;
import com.example.bookkeeping.sale.vo.SaleSummaryVO;
import com.example.bookkeeping.sale.vo.SaleVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SaleServiceImpl implements SaleService {
    private final BizSaleRecordMapper saleRecordMapper;
    private final BizSaleItemMapper saleItemMapper;
    private final BizInventoryBatchMapper inventoryBatchMapper;
    private final BizInventoryLogMapper inventoryLogMapper;
    private final BizPurchaseMapper purchaseMapper;

    public SaleServiceImpl(BizSaleRecordMapper saleRecordMapper,
                           BizSaleItemMapper saleItemMapper,
                           BizInventoryBatchMapper inventoryBatchMapper,
                           BizInventoryLogMapper inventoryLogMapper,
                           BizPurchaseMapper purchaseMapper) {
        this.saleRecordMapper = saleRecordMapper;
        this.saleItemMapper = saleItemMapper;
        this.inventoryBatchMapper = inventoryBatchMapper;
        this.inventoryLogMapper = inventoryLogMapper;
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public PageResult<SaleVO> page(SaleQueryRequest request) {
        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return PageResult.of(saleRecordMapper.selectPage(
                trimToNull(request.getKeyword()),
                request.getPaymentStatus(),
                request.getShipmentStatus(),
                request.getStartDate(),
                request.getEndDate()
        ));
    }

    @Override
    public SaleSummaryVO summary(SaleQueryRequest request) {
        return saleRecordMapper.selectSummary(
                trimToNull(request.getKeyword()),
                request.getPaymentStatus(),
                request.getShipmentStatus(),
                request.getStartDate(),
                request.getEndDate()
        );
    }

    @Override
    public List<SaleStockVO> availableStock(String keyword, List<Long> productTypeIds) {
        return inventoryBatchMapper.selectAvailableForSale(trimToNull(keyword), productTypeIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleVO create(SaveSaleRequest request) {
        String recordNo = StringUtils.hasText(request.getRecordNo()) ? request.getRecordNo().trim() : generateSaleNo();
        if (saleRecordMapper.selectByRecordNo(recordNo) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "销售单号已存在");
        }

        BigDecimal totalSaleAmount = BigDecimal.ZERO;
        BigDecimal totalCostAmount = BigDecimal.ZERO;
        Set<Long> purchaseIds = new LinkedHashSet<>();

        BizSaleRecord record = new BizSaleRecord();
        record.setRecordNo(recordNo);
        record.setBusinessDate(request.getBusinessDate());
        record.setPlatform(request.getPlatform().trim());
        record.setPlatformOrderNo(trimToNull(request.getPlatformOrderNo()));
        record.setBuyerName(trimToNull(request.getBuyerName()));
        record.setBuyerPhone(trimToNull(request.getBuyerPhone()));
        record.setPlatformFee(defaultZero(request.getPlatformFee()));
        record.setExpressFee(defaultZero(request.getExpressFee()));
        record.setPackageFee(defaultZero(request.getPackageFee()));
        record.setPromotionFee(defaultZero(request.getPromotionFee()));
        record.setRefundAmount(defaultZero(request.getRefundAmount()));
        record.setOtherExpense(defaultZero(request.getOtherExpense()));
        record.setPaymentStatus(request.getPaymentStatus() == null ? 1 : request.getPaymentStatus());
        record.setShipmentStatus(request.getShipmentStatus() == null ? 0 : request.getShipmentStatus());
        record.setExpressCompany(trimToNull(request.getExpressCompany()));
        record.setExpressNo(trimToNull(request.getExpressNo()));
        record.setRemark(trimToNull(request.getRemark()));
        record.setTotalSaleAmount(BigDecimal.ZERO);
        record.setTotalCostAmount(BigDecimal.ZERO);
        record.setReceivedAmount(BigDecimal.ZERO);
        record.setProfitAmount(BigDecimal.ZERO);
        saleRecordMapper.insert(record);

        for (SaveSaleItemRequest itemRequest : request.getItems()) {
            BizInventoryBatch batch = inventoryBatchMapper.selectForUpdate(itemRequest.getBatchId());
            if (batch == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "库存批次不存在");
            }
            if (batch.getAvailableQuantity() == null || batch.getAvailableQuantity() < itemRequest.getQuantity()) {
                throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK, "库存不足，批次：" + batch.getBatchNo());
            }

            BigDecimal saleAmount = itemRequest.getSaleUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            BigDecimal costAmount = batch.getUnitCost().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            BigDecimal lineProfit = saleAmount.subtract(costAmount);
            int beforeQuantity = batch.getAvailableQuantity();
            int afterQuantity = beforeQuantity - itemRequest.getQuantity();

            BizSaleItem item = new BizSaleItem();
            item.setSaleRecordId(record.getId());
            item.setProductId(batch.getProductId());
            item.setBatchId(batch.getId());
            item.setPurchaseId(batch.getPurchaseId());
            item.setPurchaseItemId(batch.getPurchaseItemId());
            item.setQuantity(itemRequest.getQuantity());
            item.setSaleUnitPrice(itemRequest.getSaleUnitPrice());
            item.setSaleAmount(saleAmount);
            item.setCostUnitPrice(batch.getUnitCost());
            item.setCostAmount(costAmount);
            item.setProfitAmount(lineProfit);
            item.setRemark(trimToNull(itemRequest.getRemark()));
            saleItemMapper.insert(item);

            inventoryBatchMapper.decreaseForSale(batch.getId(), itemRequest.getQuantity());
            inventoryLogMapper.insert(createSaleLog(record, item, batch, beforeQuantity, afterQuantity));
            purchaseIds.add(batch.getPurchaseId());
            totalSaleAmount = totalSaleAmount.add(saleAmount);
            totalCostAmount = totalCostAmount.add(costAmount);
        }

        BigDecimal expenseAmount = record.getPlatformFee()
                .add(record.getExpressFee())
                .add(record.getPackageFee())
                .add(record.getPromotionFee())
                .add(record.getOtherExpense());
        BigDecimal receivedAmount = totalSaleAmount.subtract(record.getRefundAmount());
        BigDecimal profitAmount = receivedAmount.subtract(totalCostAmount).subtract(expenseAmount);
        saleRecordMapper.updateTotals(record.getId(), totalSaleAmount, totalCostAmount, receivedAmount, profitAmount);
        for (Long purchaseId : purchaseIds) {
            purchaseMapper.refreshSaleStatus(purchaseId);
        }

        List<SaleVO> list = saleRecordMapper.selectPage(recordNo, null, null, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<SaleItemVO> items(Long saleRecordId) {
        return saleItemMapper.selectVOBySaleRecordId(saleRecordId);
    }

    private BizInventoryLog createSaleLog(BizSaleRecord record, BizSaleItem item, BizInventoryBatch batch, int beforeQuantity, int afterQuantity) {
        BizInventoryLog log = new BizInventoryLog();
        log.setProductId(item.getProductId());
        log.setBatchId(batch.getId());
        log.setBusinessType("SALE_OUT");
        log.setBusinessId(record.getId());
        log.setBusinessNo(record.getRecordNo());
        log.setChangeQuantity(-item.getQuantity());
        log.setBeforeQuantity(beforeQuantity);
        log.setAfterQuantity(afterQuantity);
        log.setUnitCost(batch.getUnitCost());
        log.setBusinessDate(record.getBusinessDate());
        log.setRemark("销售出库");
        return log;
    }

    private String generateSaleNo() {
        return "S" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
