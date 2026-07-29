package com.example.bookkeeping.system.menu.service;

import com.example.bookkeeping.system.menu.dto.SaveMenuRequest;
import com.example.bookkeeping.system.menu.vo.MenuVO;

import java.util.List;

public interface MenuService {

    List<MenuVO> listTree();

    List<MenuVO> listCurrentUserMenus(Long userId);

    MenuVO create(SaveMenuRequest request);

    MenuVO update(Long id, SaveMenuRequest request);

    void delete(Long id);
}
