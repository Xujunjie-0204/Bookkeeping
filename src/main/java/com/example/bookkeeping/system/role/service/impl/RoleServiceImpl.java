package com.example.bookkeeping.system.role.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.system.menu.mapper.SysMenuMapper;
import com.example.bookkeeping.system.role.dto.AssignRoleMenusRequest;
import com.example.bookkeeping.system.role.dto.SaveRoleRequest;
import com.example.bookkeeping.system.role.entity.SysRole;
import com.example.bookkeeping.system.role.mapper.SysRoleMapper;
import com.example.bookkeeping.system.role.service.RoleService;
import com.example.bookkeeping.system.role.vo.RoleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;

    public RoleServiceImpl(SysRoleMapper sysRoleMapper, SysMenuMapper sysMenuMapper) {
        this.sysRoleMapper = sysRoleMapper;
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<RoleVO> list() {
        List<SysRole> roles = sysRoleMapper.selectAll();
        List<RoleVO> result = new ArrayList<RoleVO>();
        for (SysRole role : roles) {
            result.add(toVO(role));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO create(SaveRoleRequest request) {
        validateRoleCode(request.getRoleCode(), null);
        SysRole role = toEntity(null, request);
        sysRoleMapper.insert(role);
        return toVO(sysRoleMapper.selectById(role.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO update(Long id, SaveRoleRequest request) {
        SysRole existing = sysRoleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }
        validateRoleCode(request.getRoleCode(), id);
        sysRoleMapper.update(toEntity(id, request));
        return toVO(sysRoleMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysRole existing = sysRoleMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }
        sysRoleMapper.deleteRoleMenus(id);
        sysRoleMapper.deleteById(id);
    }

    @Override
    public List<Long> getMenuIds(Long roleId) {
        ensureRoleExists(roleId);
        return sysRoleMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenus(Long roleId, AssignRoleMenusRequest request) {
        ensureRoleExists(roleId);
        sysRoleMapper.deleteRoleMenus(roleId);
        Set<Long> menuIds = new LinkedHashSet<Long>(request.getMenuIds());
        for (Long menuId : menuIds) {
            if (menuId != null) {
                if (sysMenuMapper.selectById(menuId) == null) {
                    throw new BusinessException(ErrorCode.NOT_FOUND, "菜单不存在: " + menuId);
                }
                sysRoleMapper.insertRoleMenu(roleId, menuId);
            }
        }
    }

    private void validateRoleCode(String roleCode, Long currentId) {
        SysRole existing = sysRoleMapper.selectByRoleCode(roleCode.trim());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "角色编码已存在");
        }
    }

    private void ensureRoleExists(Long roleId) {
        if (sysRoleMapper.selectById(roleId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "角色不存在");
        }
    }

    private SysRole toEntity(Long id, SaveRoleRequest request) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setRoleCode(request.getRoleCode().trim());
        role.setRoleName(request.getRoleName().trim());
        role.setStatus(request.getStatus());
        role.setRemark(trimToNull(request.getRemark()));
        return role;
    }

    private RoleVO toVO(SysRole role) {
        RoleVO vo = new RoleVO();
        vo.setId(role.getId());
        vo.setRoleCode(role.getRoleCode());
        vo.setRoleName(role.getRoleName());
        vo.setStatus(role.getStatus());
        vo.setRemark(role.getRemark());
        vo.setCreatedAt(role.getCreatedAt());
        vo.setUpdatedAt(role.getUpdatedAt());
        return vo;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
