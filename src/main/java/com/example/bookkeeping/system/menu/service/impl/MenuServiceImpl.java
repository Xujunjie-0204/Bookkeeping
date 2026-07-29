package com.example.bookkeeping.system.menu.service.impl;

import com.example.bookkeeping.common.exception.BusinessException;
import com.example.bookkeeping.common.exception.ErrorCode;
import com.example.bookkeeping.system.menu.dto.SaveMenuRequest;
import com.example.bookkeeping.system.menu.entity.SysMenu;
import com.example.bookkeeping.system.menu.mapper.SysMenuMapper;
import com.example.bookkeeping.system.menu.service.MenuService;
import com.example.bookkeeping.system.menu.vo.MenuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    private final SysMenuMapper sysMenuMapper;

    public MenuServiceImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<MenuVO> listTree() {
        return buildTree(sysMenuMapper.selectAll());
    }

    @Override
    public List<MenuVO> listCurrentUserMenus(Long userId) {
        return buildTree(sysMenuMapper.selectByUserId(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuVO create(SaveMenuRequest request) {
        validateParent(request.getParentId(), null);
        validatePermissionCode(request.getPermissionCode(), null);
        SysMenu menu = toEntity(null, request);
        sysMenuMapper.insert(menu);
        return toVO(sysMenuMapper.selectById(menu.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MenuVO update(Long id, SaveMenuRequest request) {
        SysMenu existing = sysMenuMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "菜单不存在");
        }
        validateParent(request.getParentId(), id);
        validatePermissionCode(request.getPermissionCode(), id);
        sysMenuMapper.update(toEntity(id, request));
        return toVO(sysMenuMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysMenu existing = sysMenuMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "菜单不存在");
        }
        if (sysMenuMapper.countChildren(id) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "请先删除子菜单");
        }
        sysMenuMapper.deleteById(id);
    }

    private void validateParent(Long parentId, Long currentId) {
        Long normalizedParentId = parentId == null ? 0L : parentId;
        if (currentId != null && currentId.equals(normalizedParentId)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "父菜单不能选择自己");
        }
        if (normalizedParentId.longValue() != 0L && sysMenuMapper.selectById(normalizedParentId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "父菜单不存在");
        }
    }

    private void validatePermissionCode(String permissionCode, Long currentId) {
        if (!StringUtils.hasText(permissionCode)) {
            return;
        }
        SysMenu existing = sysMenuMapper.selectByPermissionCode(permissionCode.trim());
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new BusinessException(ErrorCode.DUPLICATE_DATA, "权限标识已存在");
        }
    }

    private SysMenu toEntity(Long id, SaveMenuRequest request) {
        SysMenu menu = new SysMenu();
        menu.setId(id);
        menu.setParentId(request.getParentId() == null ? 0L : request.getParentId());
        menu.setMenuName(request.getMenuName().trim());
        menu.setMenuType(request.getMenuType());
        menu.setPath(trimToNull(request.getPath()));
        menu.setComponent(trimToNull(request.getComponent()));
        menu.setPermissionCode(trimToNull(request.getPermissionCode()));
        menu.setIcon(trimToNull(request.getIcon()));
        menu.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        menu.setVisible(request.getVisible());
        menu.setStatus(request.getStatus());
        menu.setRemark(trimToNull(request.getRemark()));
        return menu;
    }

    private List<MenuVO> buildTree(List<SysMenu> menus) {
        Map<Long, MenuVO> nodeMap = new LinkedHashMap<Long, MenuVO>();
        for (SysMenu menu : menus) {
            nodeMap.put(menu.getId(), toVO(menu));
        }
        List<MenuVO> roots = new ArrayList<MenuVO>();
        for (MenuVO node : nodeMap.values()) {
            if (node.getParentId() == null || node.getParentId().longValue() == 0L || !nodeMap.containsKey(node.getParentId())) {
                roots.add(node);
            } else {
                nodeMap.get(node.getParentId()).getChildren().add(node);
            }
        }
        return roots;
    }

    private MenuVO toVO(SysMenu menu) {
        if (menu == null) {
            return null;
        }
        MenuVO vo = new MenuVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setPath(menu.getPath());
        vo.setComponent(menu.getComponent());
        vo.setPermissionCode(menu.getPermissionCode());
        vo.setIcon(menu.getIcon());
        vo.setSortOrder(menu.getSortOrder());
        vo.setVisible(menu.getVisible());
        vo.setStatus(menu.getStatus());
        vo.setRemark(menu.getRemark());
        vo.setCreatedAt(menu.getCreatedAt());
        vo.setUpdatedAt(menu.getUpdatedAt());
        return vo;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
