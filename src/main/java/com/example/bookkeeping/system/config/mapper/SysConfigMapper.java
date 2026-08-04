package com.example.bookkeeping.system.config.mapper;

import com.example.bookkeeping.system.config.entity.SysConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysConfigMapper {

    SysConfig selectById(@Param("id") Long id);

    SysConfig selectByConfigCode(@Param("configCode") String configCode);

    List<SysConfig> selectAll();

    int insert(SysConfig config);

    int update(SysConfig config);

    int deleteById(@Param("id") Long id);
}
