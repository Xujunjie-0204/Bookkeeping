package com.example.bookkeeping.system.menu.mapper;

import com.example.bookkeeping.system.menu.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper {

    SysMenu selectById(@Param("id") Long id);

    SysMenu selectByPermissionCode(@Param("permissionCode") String permissionCode);

    List<SysMenu> selectAll();

    List<SysMenu> selectByUserId(@Param("userId") Long userId);

    int countChildren(@Param("parentId") Long parentId);

    int insert(SysMenu menu);

    int update(SysMenu menu);

    int deleteById(@Param("id") Long id);
}
