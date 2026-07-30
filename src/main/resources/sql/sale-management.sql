SET @add_sale_item_purchase_id_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE biz_sale_item ADD COLUMN purchase_id BIGINT DEFAULT NULL COMMENT ''关联采购单ID'' AFTER batch_id',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_sale_item'
    AND COLUMN_NAME = 'purchase_id'
);
PREPARE add_sale_item_purchase_id_stmt FROM @add_sale_item_purchase_id_sql;
EXECUTE add_sale_item_purchase_id_stmt;
DEALLOCATE PREPARE add_sale_item_purchase_id_stmt;

SET @add_sale_item_purchase_item_id_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE biz_sale_item ADD COLUMN purchase_item_id BIGINT DEFAULT NULL COMMENT ''关联采购明细ID'' AFTER purchase_id',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_sale_item'
    AND COLUMN_NAME = 'purchase_item_id'
);
PREPARE add_sale_item_purchase_item_id_stmt FROM @add_sale_item_purchase_item_id_sql;
EXECUTE add_sale_item_purchase_item_id_stmt;
DEALLOCATE PREPARE add_sale_item_purchase_item_id_stmt;

SET @add_sale_item_purchase_id_idx_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE biz_sale_item ADD INDEX idx_biz_sale_item_purchase_id (purchase_id)',
    'SELECT 1'
  )
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_sale_item'
    AND INDEX_NAME = 'idx_biz_sale_item_purchase_id'
);
PREPARE add_sale_item_purchase_id_idx_stmt FROM @add_sale_item_purchase_id_idx_sql;
EXECUTE add_sale_item_purchase_id_idx_stmt;
DEALLOCATE PREPARE add_sale_item_purchase_id_idx_stmt;

SET @add_sale_item_purchase_item_id_idx_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE biz_sale_item ADD INDEX idx_biz_sale_item_purchase_item_id (purchase_item_id)',
    'SELECT 1'
  )
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_sale_item'
    AND INDEX_NAME = 'idx_biz_sale_item_purchase_item_id'
);
PREPARE add_sale_item_purchase_item_id_idx_stmt FROM @add_sale_item_purchase_item_id_idx_sql;
EXECUTE add_sale_item_purchase_item_id_idx_stmt;
DEALLOCATE PREPARE add_sale_item_purchase_item_id_idx_stmt;

INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission_code, icon, sort_order, status, visible, remark, created_at, updated_at, deleted)
SELECT 25, 0, '销售管理', 'C', '/sales', 'SaleManageView', 'sale:view', 'Sell', 45, 1, 1, NULL, NOW(), NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 25);

UPDATE sys_menu
SET menu_name = '销售管理',
    path = '/sales',
    component = 'SaleManageView',
    permission_code = 'sale:view',
    icon = 'Sell',
    sort_order = 45,
    status = 1,
    visible = 1,
    updated_at = NOW(),
    deleted = 0
WHERE id = 25;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id, created_at)
SELECT 1, 25, NOW()
FROM sys_role
WHERE id = 1;
