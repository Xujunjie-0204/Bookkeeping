package com.example.bookkeeping.system.config.service;

import com.example.bookkeeping.system.config.dto.SaveConfigRequest;
import com.example.bookkeeping.system.config.vo.ConfigVO;

import java.util.List;

public interface ConfigService {

    List<ConfigVO> list();

    ConfigVO create(SaveConfigRequest request);

    ConfigVO update(Long id, SaveConfigRequest request);

    void delete(Long id);
}
