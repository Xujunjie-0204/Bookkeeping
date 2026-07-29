USE bookkeeping;

CREATE TABLE IF NOT EXISTS biz_product_type (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  type_code VARCHAR(50) NOT NULL COMMENT '类型编码',
  type_name VARCHAR(100) NOT NULL COMMENT '类型名称',
  parent_id BIGINT NOT NULL DEFAULT 0 COMMENT '父类型ID，0表示根节点',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用，0停用',
  remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  PRIMARY KEY (id),
  UNIQUE KEY uk_biz_product_type_code (type_code),
  KEY idx_biz_product_type_parent_id (parent_id),
  KEY idx_biz_product_type_name (type_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品类型';

SET @add_product_type_id_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_product ADD COLUMN product_type_id BIGINT DEFAULT NULL COMMENT ''商品类型ID'' AFTER product_name',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_product'
    AND COLUMN_NAME = 'product_type_id'
);
PREPARE add_product_type_id_stmt FROM @add_product_type_id_sql;
EXECUTE add_product_type_id_stmt;
DEALLOCATE PREPARE add_product_type_id_stmt;

SET @add_product_type_idx_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_product ADD KEY idx_biz_product_type_id (product_type_id)',
    'SELECT 1'
  )
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_product'
    AND INDEX_NAME = 'idx_biz_product_type_id'
);
PREPARE add_product_type_idx_stmt FROM @add_product_type_idx_sql;
EXECUTE add_product_type_idx_stmt;
DEALLOCATE PREPARE add_product_type_idx_stmt;

INSERT INTO biz_product_type (
  type_code, type_name, parent_id, sort_order, status, remark, created_at, updated_at, deleted
) VALUES (
  'default', '默认类型', 0, 0, 1, '系统默认商品类型', NOW(), NOW(), 0
) ON DUPLICATE KEY UPDATE
  type_name = VALUES(type_name),
  status = VALUES(status),
  updated_at = NOW(),
  deleted = 0;
