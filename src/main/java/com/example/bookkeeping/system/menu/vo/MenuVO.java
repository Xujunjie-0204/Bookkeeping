package com.example.bookkeeping.system.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "菜单信息")
public class MenuVO {

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String path;
    private String component;
    private String permissionCode;
    private String icon;
    private Integer sortOrder;
    private Integer visible;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MenuVO> children = new ArrayList<MenuVO>();
}
