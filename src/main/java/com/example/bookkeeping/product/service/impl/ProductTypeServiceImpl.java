package com.example.bookkeeping.product.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.product.dto.SaveProductTypeRequest;
import com.example.bookkeeping.product.entity.BizProductType;
import com.example.bookkeeping.product.mapper.BizProductTypeMapper;
import com.example.bookkeeping.product.service.ProductTypeService;
import com.example.bookkeeping.product.vo.ProductTypeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final BizProductTypeMapper productTypeMapper;

    public ProductTypeServiceImpl(BizProductTypeMapper productTypeMapper) {
        this.productTypeMapper = productTypeMapper;
    }

    @Override
    public List<ProductTypeVO> listTree() {
        return buildTree(productTypeMapper.selectAll());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTypeVO create(SaveProductTypeRequest request) {
        validateParent(request.getParentId(), null);
        validateTypeCode(request.getTypeCode(), null);
        BizProductType productType = toEntity(null, request);
        productTypeMapper.insert(productType);
        return toVO(productTypeMapper.selectById(productType.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductTypeVO update(Long id, SaveProductTypeRequest request) {
        BizProductType existing = productTypeMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "商品类型不存在");
        }
        validateParent(request.getParentId(), id);
        validateTypeCode(request.getTypeCode(), id);
        productTypeMapper.update(toEntity(id, request));
        return toVO(productTypeMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        BizProductType existing = productTypeMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "商品类型不存在");
        }
        if (productTypeMapper.countChildren(id) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请先删除子类型");
        }
        productTypeMapper.deleteById(id);
    }

    private void validateParent(Long parentId, Long currentId) {
        Long normalizedParentId = parentId == null ? 0L : parentId;
        if (currentId != null && currentId.equals(normalizedParentId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "父类型不能选择自己");
        }
        if (normalizedParentId.longValue() != 0L && productTypeMapper.selectById(normalizedParentId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "父类型不存在");
        }
    }

    private void validateTypeCode(String typeCode, Long currentId) {
        BizProductType existing = productTypeMapper.selectByTypeCode(typeCode.trim());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "类型编码已存在");
        }
    }

    private BizProductType toEntity(Long id, SaveProductTypeRequest request) {
        BizProductType productType = new BizProductType();
        productType.setId(id);
        productType.setParentId(request.getParentId() == null ? 0L : request.getParentId());
        productType.setTypeCode(request.getTypeCode().trim());
        productType.setTypeName(request.getTypeName().trim());
        productType.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        productType.setStatus(request.getStatus());
        productType.setRemark(trimToNull(request.getRemark()));
        return productType;
    }

    private List<ProductTypeVO> buildTree(List<BizProductType> productTypes) {
        Map<Long, ProductTypeVO> nodeMap = new LinkedHashMap<Long, ProductTypeVO>();
        for (BizProductType productType : productTypes) {
            nodeMap.put(productType.getId(), toVO(productType));
        }
        List<ProductTypeVO> roots = new ArrayList<ProductTypeVO>();
        for (ProductTypeVO node : nodeMap.values()) {
            if (node.getParentId() == null || node.getParentId().longValue() == 0L || !nodeMap.containsKey(node.getParentId())) {
                roots.add(node);
            } else {
                nodeMap.get(node.getParentId()).getChildren().add(node);
            }
        }
        return roots;
    }

    private ProductTypeVO toVO(BizProductType productType) {
        if (productType == null) {
            return null;
        }
        ProductTypeVO vo = new ProductTypeVO();
        vo.setId(productType.getId());
        vo.setTypeCode(productType.getTypeCode());
        vo.setTypeName(productType.getTypeName());
        vo.setParentId(productType.getParentId());
        vo.setSortOrder(productType.getSortOrder());
        vo.setStatus(productType.getStatus());
        vo.setRemark(productType.getRemark());
        vo.setCreatedAt(productType.getCreatedAt());
        vo.setUpdatedAt(productType.getUpdatedAt());
        return vo;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
