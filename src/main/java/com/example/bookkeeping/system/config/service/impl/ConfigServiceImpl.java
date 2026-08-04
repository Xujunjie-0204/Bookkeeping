package com.example.bookkeeping.system.config.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.system.config.dto.SaveConfigRequest;
import com.example.bookkeeping.system.config.entity.SysConfig;
import com.example.bookkeeping.system.config.mapper.SysConfigMapper;
import com.example.bookkeeping.system.config.service.ConfigService;
import com.example.bookkeeping.system.config.vo.ConfigVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final SysConfigMapper sysConfigMapper;
    private final ObjectMapper objectMapper;

    public ConfigServiceImpl(SysConfigMapper sysConfigMapper, ObjectMapper objectMapper) {
        this.sysConfigMapper = sysConfigMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ConfigVO> list() {
        List<SysConfig> configs = sysConfigMapper.selectAll();
        List<ConfigVO> result = new ArrayList<ConfigVO>();
        for (SysConfig config : configs) {
            result.add(toVO(config));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigVO create(SaveConfigRequest request) {
        validateConfigCode(request.getConfigCode(), null);
        validateJson(request.getConfigValue());
        SysConfig config = toEntity(null, request);
        sysConfigMapper.insert(config);
        return toVO(sysConfigMapper.selectById(config.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigVO update(Long id, SaveConfigRequest request) {
        SysConfig existing = sysConfigMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "系统配置不存在");
        }
        validateConfigCode(request.getConfigCode(), id);
        validateJson(request.getConfigValue());
        sysConfigMapper.update(toEntity(id, request));
        return toVO(sysConfigMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysConfig existing = sysConfigMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "系统配置不存在");
        }
        sysConfigMapper.deleteById(id);
    }

    private void validateConfigCode(String configCode, Long currentId) {
        SysConfig existing = sysConfigMapper.selectByConfigCode(configCode.trim());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "配置编码已存在");
        }
    }

    private void validateJson(String value) {
        try {
            objectMapper.readTree(value);
        } catch (Exception ex) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "配置数据必须是合法JSON");
        }
    }

    private SysConfig toEntity(Long id, SaveConfigRequest request) {
        SysConfig config = new SysConfig();
        config.setId(id);
        config.setConfigCode(request.getConfigCode().trim());
        config.setConfigName(request.getConfigName().trim());
        config.setConfigValue(request.getConfigValue().trim());
        config.setStatus(request.getStatus());
        config.setRemark(trimToNull(request.getRemark()));
        return config;
    }

    private ConfigVO toVO(SysConfig config) {
        ConfigVO vo = new ConfigVO();
        vo.setId(config.getId());
        vo.setConfigCode(config.getConfigCode());
        vo.setConfigName(config.getConfigName());
        vo.setConfigValue(config.getConfigValue());
        vo.setStatus(config.getStatus());
        vo.setRemark(config.getRemark());
        vo.setCreatedAt(config.getCreatedAt());
        vo.setUpdatedAt(config.getUpdatedAt());
        return vo;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
