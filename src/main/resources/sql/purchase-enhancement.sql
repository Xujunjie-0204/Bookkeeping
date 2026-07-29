USE bookkeeping;

SET @add_purchase_item_count_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase ADD COLUMN item_count INT NOT NULL DEFAULT 1 COMMENT ''采购件数，二手单件采购通常为1'' AFTER purchase_date',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase'
    AND COLUMN_NAME = 'item_count'
);
PREPARE add_purchase_item_count_stmt FROM @add_purchase_item_count_sql;
EXECUTE add_purchase_item_count_stmt;
DEALLOCATE PREPARE add_purchase_item_count_stmt;

SET @add_purchase_status_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase ADD COLUMN purchase_status TINYINT NOT NULL DEFAULT 1 COMMENT ''采购状态：1待到货，2已到货，3已取消'' AFTER item_count',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase'
    AND COLUMN_NAME = 'purchase_status'
);
PREPARE add_purchase_status_stmt FROM @add_purchase_status_sql;
EXECUTE add_purchase_status_stmt;
DEALLOCATE PREPARE add_purchase_status_stmt;

SET @add_seller_account_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase ADD COLUMN seller_account VARCHAR(100) DEFAULT NULL COMMENT ''卖家账号/昵称'' AFTER supplier_name',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase'
    AND COLUMN_NAME = 'seller_account'
);
PREPARE add_seller_account_stmt FROM @add_seller_account_sql;
EXECUTE add_seller_account_stmt;
DEALLOCATE PREPARE add_seller_account_stmt;

SET @add_receive_at_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase ADD COLUMN received_at DATETIME DEFAULT NULL COMMENT ''到货时间'' AFTER payment_method',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase'
    AND COLUMN_NAME = 'received_at'
);
PREPARE add_receive_at_stmt FROM @add_receive_at_sql;
EXECUTE add_receive_at_stmt;
DEALLOCATE PREPARE add_receive_at_stmt;

SET @add_purchase_item_condition_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase_item ADD COLUMN condition_desc VARCHAR(200) DEFAULT NULL COMMENT ''成色/瑕疵描述'' AFTER product_id',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase_item'
    AND COLUMN_NAME = 'condition_desc'
);
PREPARE add_purchase_item_condition_stmt FROM @add_purchase_item_condition_sql;
EXECUTE add_purchase_item_condition_stmt;
DEALLOCATE PREPARE add_purchase_item_condition_stmt;

SET @add_purchase_item_device_no_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase_item ADD COLUMN device_no VARCHAR(100) DEFAULT NULL COMMENT ''单台设备唯一编号，如IMEI/序列号'' AFTER condition_desc',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase_item'
    AND COLUMN_NAME = 'device_no'
);
PREPARE add_purchase_item_device_no_stmt FROM @add_purchase_item_device_no_sql;
EXECUTE add_purchase_item_device_no_stmt;
DEALLOCATE PREPARE add_purchase_item_device_no_stmt;

SET @add_purchase_item_check_status_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase_item ADD COLUMN check_status TINYINT NOT NULL DEFAULT 0 COMMENT ''验货状态：0未验货，1通过，2异常'' AFTER total_amount',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase_item'
    AND COLUMN_NAME = 'check_status'
);
PREPARE add_purchase_item_check_status_stmt FROM @add_purchase_item_check_status_sql;
EXECUTE add_purchase_item_check_status_stmt;
DEALLOCATE PREPARE add_purchase_item_check_status_stmt;

SET @add_purchase_item_device_idx_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_purchase_item ADD KEY idx_biz_purchase_item_device_no (device_no)',
    'SELECT 1'
  )
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_purchase_item'
    AND INDEX_NAME = 'idx_biz_purchase_item_device_no'
);
PREPARE add_purchase_item_device_idx_stmt FROM @add_purchase_item_device_idx_sql;
EXECUTE add_purchase_item_device_idx_stmt;
DEALLOCATE PREPARE add_purchase_item_device_idx_stmt;
