package com.example.bookkeeping.system.role.mapper;

import com.example.bookkeeping.system.role.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {

    SysRole selectById(@Param("id") Long id);

    SysRole selectByRoleCode(@Param("roleCode") String roleCode);

    List<SysRole> selectAll();

    List<SysRole> selectByUserId(@Param("userId") Long userId);

    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    int insert(SysRole role);

    int update(SysRole role);

    int deleteById(@Param("id") Long id);

    int deleteRoleMenus(@Param("roleId") Long roleId);

    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}
