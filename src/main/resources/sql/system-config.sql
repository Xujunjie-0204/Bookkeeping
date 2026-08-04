USE bookkeeping;

CREATE TABLE IF NOT EXISTS sys_config (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  config_code VARCHAR(80) NOT NULL COMMENT 'config code',
  config_name VARCHAR(80) NOT NULL COMMENT 'config name',
  config_value JSON NOT NULL COMMENT 'config json value',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '1 enabled, 0 disabled',
  remark VARCHAR(500) DEFAULT NULL COMMENT 'remark',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'created time',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'updated time',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT 'logical delete flag',
  PRIMARY KEY (id),
  UNIQUE KEY uk_sys_config_code (config_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='system config';

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

INSERT INTO sys_menu (
  id, parent_id, menu_name, menu_type, path, component, permission_code,
  icon, sort_order, visible, status, remark, created_at, updated_at, deleted
) VALUES
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
WHERE id IN (27, 28, 29, 30)
  AND deleted = 0;
