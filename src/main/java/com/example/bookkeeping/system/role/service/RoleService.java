package com.example.bookkeeping.system.role.service;

import com.example.bookkeeping.system.role.dto.AssignRoleMenusRequest;
import com.example.bookkeeping.system.role.dto.SaveRoleRequest;
import com.example.bookkeeping.system.role.vo.RoleVO;

import java.util.List;

public interface RoleService {

    List<RoleVO> list();

    RoleVO create(SaveRoleRequest request);

    RoleVO update(Long id, SaveRoleRequest request);

    void delete(Long id);

    List<Long> getMenuIds(Long roleId);

    void assignMenus(Long roleId, AssignRoleMenusRequest request);
}
