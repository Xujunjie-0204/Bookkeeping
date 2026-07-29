package com.example.bookkeeping.product.product.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.common.page.PageResult;
import com.example.bookkeeping.product.product.dto.ProductQueryRequest;
import com.example.bookkeeping.product.product.dto.SaveProductRequest;
import com.example.bookkeeping.product.product.entity.BizProduct;
import com.example.bookkeeping.product.product.mapper.BizProductMapper;
import com.example.bookkeeping.product.product.service.ProductService;
import com.example.bookkeeping.product.product.vo.ProductVO;
import com.example.bookkeeping.product.type.entity.BizProductType;
import com.example.bookkeeping.product.type.mapper.BizProductTypeMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final BizProductMapper productMapper;
    private final BizProductTypeMapper productTypeMapper;

    public ProductServiceImpl(BizProductMapper productMapper, BizProductTypeMapper productTypeMapper) {
        this.productMapper = productMapper;
        this.productTypeMapper = productTypeMapper;
    }

    @Override
    public PageResult<ProductVO> page(ProductQueryRequest request) {
        int pageNum = request.getPageNum() == null || request.getPageNum() < 1 ? 1 : request.getPageNum();
        int pageSize = request.getPageSize() == null || request.getPageSize() < 1 ? 10 : request.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<ProductVO> list = productMapper.selectPage(
                trimToNull(request.getKeyword()),
                request.getProductTypeId(),
                request.getStatus()
        );
        return PageResult.of(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO create(SaveProductRequest request) {
        validateProductCode(request.getProductCode(), null);
        BizProduct product = toEntity(null, request);
        productMapper.insert(product);
        return findVO(product.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductVO update(Long id, SaveProductRequest request) {
        BizProduct existing = productMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        validateProductCode(request.getProductCode(), id);
        productMapper.update(toEntity(id, request));
        return findVO(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        BizProduct existing = productMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "商品不存在");
        }
        productMapper.deleteById(id);
    }

    private void validateProductCode(String productCode, Long currentId) {
        BizProduct existing = productMapper.selectByProductCode(productCode.trim());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "商品编码已存在");
        }
    }

    private BizProduct toEntity(Long id, SaveProductRequest request) {
        BizProduct product = new BizProduct();
        product.setId(id);
        product.setProductCode(request.getProductCode().trim());
        product.setProductName(request.getProductName().trim());
        product.setProductTypeId(request.getProductTypeId());
        product.setCategoryName(resolveCategoryName(request.getProductTypeId()));
        product.setBrand(trimToNull(request.getBrand()));
        product.setModel(trimToNull(request.getModel()));
        product.setSpecification(trimToNull(request.getSpecification()));
        product.setDefaultCost(request.getDefaultCost());
        product.setDefaultSalePrice(request.getDefaultSalePrice());
        product.setWarningStock(request.getWarningStock());
        product.setStatus(request.getStatus());
        product.setRemark(trimToNull(request.getRemark()));
        return product;
    }

    private String resolveCategoryName(Long productTypeId) {
        if (productTypeId == null) {
            return null;
        }
        BizProductType productType = productTypeMapper.selectById(productTypeId);
        if (productType == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "商品类型不存在");
        }
        return productType.getTypeName();
    }

    private ProductVO findVO(Long id) {
        ProductVO product = productMapper.selectVOById(id);
        if (product != null) {
            return product;
        }
        throw new BusinessException(ErrorCode.NOT_FOUND, "商品不存在");
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
