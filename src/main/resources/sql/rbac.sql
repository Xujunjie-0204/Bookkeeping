USE bookkeeping;

CREATE TABLE IF NOT EXISTS sys_menu (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID，0表示根节点',
  menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
  menu_type CHAR(1) NOT NULL COMMENT '菜单类型：M目录，C菜单，F按钮',
  path VARCHAR(120) DEFAULT NULL COMMENT '路由地址',
  component VARCHAR(120) DEFAULT NULL COMMENT '组件路径',
  permission_code VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  icon VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  visible TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示：1显示，0隐藏',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0停用',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sys_menu_permission_code (permission_code),
  KEY idx_sys_menu_parent_id (parent_id),
  KEY idx_sys_menu_sort (parent_id, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统菜单权限';

CREATE TABLE IF NOT EXISTS sys_role (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0停用',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sys_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色';

CREATE TABLE IF NOT EXISTS sys_user_role (
  user_id BIGINT NOT NULL COMMENT '用户ID',
  role_id BIGINT NOT NULL COMMENT '角色ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (user_id, role_id),
  KEY idx_sys_user_role_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联';

CREATE TABLE IF NOT EXISTS sys_role_menu (
  role_id BIGINT NOT NULL COMMENT '角色ID',
  menu_id BIGINT NOT NULL COMMENT '菜单ID',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (role_id, menu_id),
  KEY idx_sys_role_menu_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单权限关联';

INSERT INTO sys_role (
  id, role_code, role_name, status, remark, created_at, updated_at, deleted
) VALUES (
  1, 'admin', '超级管理员', 1, '系统内置管理员角色', NOW(), NOW(), 0
) ON DUPLICATE KEY UPDATE
  role_name = VALUES(role_name),
  status = VALUES(status),
  remark = VALUES(remark),
  updated_at = NOW(),
  deleted = 0;

INSERT IGNORE INTO sys_user_role (user_id, role_id, created_at)
SELECT u.id, 1, NOW()
FROM sys_user u
WHERE u.username = 'admin'
  AND u.deleted = 0;

INSERT INTO sys_menu (
  id, parent_id, menu_name, menu_type, path, component, permission_code,
  icon, sort_order, visible, status, remark, created_at, updated_at, deleted
) VALUES
  (1, 0, '首页仪表盘', 'C', '/dashboard', 'DashboardView', 'dashboard:view', 'DataLine', 10, 1, 1, NULL, NOW(), NOW(), 0),
  (12, 0, '商品管理', 'M', '/product', NULL, 'product:manage', 'Goods', 20, 1, 1, NULL, NOW(), NOW(), 0),
  (13, 12, '商品列表', 'C', '/products', 'ProductManageView', 'product:view', 'Goods', 10, 1, 1, NULL, NOW(), NOW(), 0),
  (14, 13, '新增商品', 'F', NULL, NULL, 'product:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (15, 13, '修改商品', 'F', NULL, NULL, 'product:update', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (16, 13, '删除商品', 'F', NULL, NULL, 'product:delete', NULL, 30, 0, 1, NULL, NOW(), NOW(), 0),
  (17, 12, '商品类型', 'C', '/product-types', 'ProductTypeManageView', 'product-type:view', 'CollectionTag', 20, 1, 1, NULL, NOW(), NOW(), 0),
  (18, 17, '新增商品类型', 'F', NULL, NULL, 'product-type:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (19, 17, '修改商品类型', 'F', NULL, NULL, 'product-type:update', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (20, 17, '删除商品类型', 'F', NULL, NULL, 'product-type:delete', NULL, 30, 0, 1, NULL, NOW(), NOW(), 0),
  (21, 0, '采购进货', 'C', '/purchases', 'PurchaseManageView', 'purchase:view', 'ShoppingCart', 30, 1, 1, NULL, NOW(), NOW(), 0),
  (22, 21, '新增进货', 'F', NULL, NULL, 'purchase:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (23, 21, '删除进货', 'F', NULL, NULL, 'purchase:delete', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (24, 0, '库存管理', 'C', '/inventory', 'InventoryManageView', 'inventory:view', 'Box', 35, 1, 1, NULL, NOW(), NOW(), 0),
  (2, 0, '系统管理', 'M', '/system', NULL, 'system:view', 'Setting', 90, 1, 1, NULL, NOW(), NOW(), 0),
  (3, 2, '菜单管理', 'C', '/system/menus', 'MenuManageView', 'system:menu:view', 'Menu', 10, 1, 1, NULL, NOW(), NOW(), 0),
  (4, 3, '新增菜单', 'F', NULL, NULL, 'system:menu:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (5, 3, '修改菜单', 'F', NULL, NULL, 'system:menu:update', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (6, 3, '删除菜单', 'F', NULL, NULL, 'system:menu:delete', NULL, 30, 0, 1, NULL, NOW(), NOW(), 0),
  (7, 2, '角色管理', 'C', '/system/roles', 'RoleManageView', 'system:role:view', 'UserFilled', 20, 1, 1, NULL, NOW(), NOW(), 0),
  (8, 7, '新增角色', 'F', NULL, NULL, 'system:role:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (9, 7, '修改角色', 'F', NULL, NULL, 'system:role:update', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (10, 7, '删除角色', 'F', NULL, NULL, 'system:role:delete', NULL, 30, 0, 1, NULL, NOW(), NOW(), 0),
  (11, 7, '分配权限', 'F', NULL, NULL, 'system:role:assign-menu', NULL, 40, 0, 1, NULL, NOW(), NOW(), 0),
  (27, 2, '系统配置', 'C', '/system/configs', 'ConfigManageView', 'system:config:view', 'Setting', 30, 1, 1, NULL, NOW(), NOW(), 0),
  (28, 27, '新增系统配置', 'F', NULL, NULL, 'system:config:create', NULL, 10, 0, 1, NULL, NOW(), NOW(), 0),
  (29, 27, '修改系统配置', 'F', NULL, NULL, 'system:config:update', NULL, 20, 0, 1, NULL, NOW(), NOW(), 0),
  (30, 27, '删除系统配置', 'F', NULL, NULL, 'system:config:delete', NULL, 30, 0, 1, NULL, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
  parent_id = VALUES(parent_id),
  menu_name = VALUES(menu_name),
  menu_type = VALUES(menu_type),
  path = VALUES(path),
  component = VALUES(component),
  permission_code = VALUES(permission_code),
  icon = VALUES(icon),
  sort_order = VALUES(sort_order),
  visible = VALUES(visible),
  status = VALUES(status),
  updated_at = NOW(),
  deleted = 0;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id, created_at)
SELECT 1, id, NOW()
FROM sys_menu
WHERE deleted = 0;

CREATE TABLE IF NOT EXISTS sys_config (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  config_code VARCHAR(80) NOT NULL COMMENT '配置编码',
  config_name VARCHAR(80) NOT NULL COMMENT '配置名称',
  config_value JSON NOT NULL COMMENT '配置JSON',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0停用',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sys_config_code (config_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置';

INSERT INTO sys_config (
  config_code, config_name, config_value, status, remark, created_at, updated_at, deleted
) VALUES (
  'employee_management', '员工管理', JSON_ARRAY('员工A', '员工B'), 1, '简单员工字符串集合', NOW(), NOW(), 0
) ON DUPLICATE KEY UPDATE
  config_name = VALUES(config_name),
  status = VALUES(status),
  remark = VALUES(remark),
  updated_at = NOW(),
  deleted = 0;
