package com.example.bookkeeping.purchase.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.inventory.entity.BizInventoryBatch;
import com.example.bookkeeping.inventory.entity.BizInventoryLog;
import com.example.bookkeeping.inventory.mapper.BizInventoryBatchMapper;
import com.example.bookkeeping.inventory.mapper.BizInventoryLogMapper;
import com.example.bookkeeping.product.mapper.BizProductMapper;
import com.example.bookkeeping.purchase.dto.PurchaseQueryRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseItemRequest;
import com.example.bookkeeping.purchase.dto.SavePurchaseRequest;
import com.example.bookkeeping.purchase.dto.UpdatePurchaseItemRequest;
import com.example.bookkeeping.purchase.dto.UpdatePurchaseRequest;
import com.example.bookkeeping.purchase.entity.BizPurchase;
import com.example.bookkeeping.purchase.entity.BizPurchaseItem;
import com.example.bookkeeping.purchase.mapper.BizPurchaseItemMapper;
import com.example.bookkeeping.purchase.mapper.BizPurchaseMapper;
import com.example.bookkeeping.purchase.service.PurchaseService;
import com.example.bookkeeping.purchase.vo.PurchaseItemVO;
import com.example.bookkeeping.purchase.vo.PurchaseSummaryVO;
import com.example.bookkeeping.purchase.vo.PurchaseVO;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    private final BizPurchaseMapper purchaseMapper;
    private final BizPurchaseItemMapper purchaseItemMapper;
    private final BizInventoryBatchMapper inventoryBatchMapper;
    private final BizInventoryLogMapper inventoryLogMapper;
    private final BizProductMapper productMapper;

    public PurchaseServiceImpl(BizPurchaseMapper purchaseMapper,
                               BizPurchaseItemMapper purchaseItemMapper,
                               BizInventoryBatchMapper inventoryBatchMapper,
                               BizInventoryLogMapper inventoryLogMapper,
                               BizProductMapper productMapper) {
        this.purchaseMapper = purchaseMapper;
        this.purchaseItemMapper = purchaseItemMapper;
        this.inventoryBatchMapper = inventoryBatchMapper;
        this.inventoryLogMapper = inventoryLogMapper;
        this.productMapper = productMapper;
    }

    @Override
    public PageResult<PurchaseVO> page(PurchaseQueryRequest request) {
        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        return PageResult.of(purchaseMapper.selectPage(
                trimToNull(request.getKeyword()),
                request.getPurchaseStatus(),
                request.getStartDate(),
                request.getEndDate(),
                request.getProductIds()
        ));
    }

    @Override
    public PurchaseSummaryVO summary(PurchaseQueryRequest request) {
        return purchaseMapper.selectSummary(
                trimToNull(request.getKeyword()),
                request.getPurchaseStatus(),
                request.getStartDate(),
                request.getEndDate(),
                request.getProductIds()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseVO create(SavePurchaseRequest request) {
        String purchaseNo = StringUtils.hasText(request.getPurchaseNo()) ? request.getPurchaseNo().trim() : generatePurchaseNo();
        if (purchaseMapper.selectByPurchaseNo(purchaseNo) != null) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "采购单号已存在");
        }

        BigDecimal goodsAmount = BigDecimal.ZERO;
        int itemCount = 0;
        for (SavePurchaseItemRequest item : request.getItems()) {
            if (productMapper.selectById(item.getProductId()) == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND, "商品不存在: " + item.getProductId());
            }
            goodsAmount = goodsAmount.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            itemCount += item.getQuantity();
        }

        BizPurchase purchase = new BizPurchase();
        purchase.setPurchaseNo(purchaseNo);
        purchase.setPlatform(request.getPlatform().trim());
        purchase.setPlatformOrderNo(trimToNull(request.getPlatformOrderNo()));
        purchase.setSupplierName(trimToNull(request.getSupplierName()));
        purchase.setSellerAccount(trimToNull(request.getSellerAccount()));
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setItemCount(itemCount);
        purchase.setPurchaseStatus(2);
        purchase.setGoodsAmount(goodsAmount);
        purchase.setFreightAmount(defaultZero(request.getFreightAmount()));
        purchase.setDiscountAmount(defaultZero(request.getDiscountAmount()));
        purchase.setOtherAmount(defaultZero(request.getOtherAmount()));
        purchase.setPayAmount(goodsAmount.add(purchase.getFreightAmount()).add(purchase.getOtherAmount()).subtract(purchase.getDiscountAmount()));
        purchase.setInvoiceStatus(0);
        purchase.setPaymentMethod(trimToNull(request.getPaymentMethod()));
        purchase.setReceivedAt(LocalDateTime.now());
        purchase.setRemark(trimToNull(request.getRemark()));
        purchaseMapper.insert(purchase);

        for (SavePurchaseItemRequest itemRequest : request.getItems()) {
            BizPurchaseItem item = new BizPurchaseItem();
            item.setPurchaseId(purchase.getId());
            item.setProductId(itemRequest.getProductId());
            item.setConditionDesc(trimToNull(itemRequest.getConditionDesc()));
            item.setDeviceNo(trimToNull(itemRequest.getDeviceNo()));
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            item.setTotalAmount(itemRequest.getUnitPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            item.setCheckStatus(0);
            item.setRemark(trimToNull(itemRequest.getRemark()));
            purchaseItemMapper.insert(item);

            BizInventoryBatch batch = createBatch(purchase, item);
            inventoryBatchMapper.insert(batch);
            inventoryLogMapper.insert(createInventoryLog(purchase, item, batch));
        }

        List<PurchaseVO> list = purchaseMapper.selectPage(purchaseNo, null, null, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseVO update(Long id, UpdatePurchaseRequest request) {
        BizPurchase existing = purchaseMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "采购单不存在");
        }

        BizPurchase purchase = new BizPurchase();
        purchase.setId(id);
        purchase.setPlatform(request.getPlatform().trim());
        purchase.setPlatformOrderNo(trimToNull(request.getPlatformOrderNo()));
        purchase.setSupplierName(trimToNull(request.getSupplierName()));
        purchase.setSellerAccount(trimToNull(request.getSellerAccount()));
        purchase.setPurchaseDate(request.getPurchaseDate());
        purchase.setPaymentMethod(trimToNull(request.getPaymentMethod()));
        purchase.setRemark(trimToNull(request.getRemark()));
        purchaseMapper.updateEditableById(purchase);
        inventoryBatchMapper.updatePurchaseDate(id, request.getPurchaseDate());
        inventoryLogMapper.updatePurchaseInBusinessDate(id, request.getPurchaseDate());

        for (UpdatePurchaseItemRequest itemRequest : request.getItems()) {
            BizPurchaseItem item = new BizPurchaseItem();
            item.setId(itemRequest.getId());
            item.setPurchaseId(id);
            item.setConditionDesc(trimToNull(itemRequest.getConditionDesc()));
            item.setDeviceNo(trimToNull(itemRequest.getDeviceNo()));
            item.setRemark(trimToNull(itemRequest.getRemark()));
            purchaseItemMapper.updateEditableById(item);
        }

        List<PurchaseVO> list = purchaseMapper.selectPage(existing.getPurchaseNo(), null, null, null, null);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<PurchaseItemVO> items(Long id) {
        if (purchaseMapper.selectById(id) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "采购单不存在");
        }
        return purchaseItemMapper.selectVOByPurchaseId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (purchaseMapper.selectById(id) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "采购单不存在");
        }
        inventoryLogMapper.deletePurchaseInLogs(id);
        inventoryBatchMapper.deleteByPurchaseId(id);
        purchaseItemMapper.deleteByPurchaseId(id);
        purchaseMapper.deleteById(id);
    }

    private BizInventoryBatch createBatch(BizPurchase purchase, BizPurchaseItem item) {
        BizInventoryBatch batch = new BizInventoryBatch();
        batch.setBatchNo("B" + purchase.getPurchaseNo() + "-" + item.getId());
        batch.setProductId(item.getProductId());
        batch.setPurchaseId(purchase.getId());
        batch.setPurchaseItemId(item.getId());
        batch.setPurchaseDate(purchase.getPurchaseDate());
        batch.setUnitCost(item.getUnitPrice());
        batch.setInitialQuantity(item.getQuantity());
        batch.setAvailableQuantity(item.getQuantity());
        batch.setSoldQuantity(0);
        batch.setLockedQuantity(0);
        batch.setStatus(1);
        batch.setRemark(item.getRemark());
        return batch;
    }

    private BizInventoryLog createInventoryLog(BizPurchase purchase, BizPurchaseItem item, BizInventoryBatch batch) {
        BizInventoryLog log = new BizInventoryLog();
        log.setProductId(item.getProductId());
        log.setBatchId(batch.getId());
        log.setBusinessType("PURCHASE_IN");
        log.setBusinessId(purchase.getId());
        log.setBusinessNo(purchase.getPurchaseNo());
        log.setChangeQuantity(item.getQuantity());
        log.setBeforeQuantity(0);
        log.setAfterQuantity(item.getQuantity());
        log.setUnitCost(item.getUnitPrice());
        log.setBusinessDate(purchase.getPurchaseDate());
        log.setRemark("采购入库");
        return log;
    }

    private String generatePurchaseNo() {
        return "P" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
