USE bookkeeping;

SET @add_sale_fee_config_sql = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE biz_sale_record ADD COLUMN fee_config TEXT DEFAULT NULL COMMENT ''费用配置JSON，记录费率、无忧卖和费用明细'' AFTER other_expense',
    'SELECT 1'
  )
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'biz_sale_record'
    AND COLUMN_NAME = 'fee_config'
);
PREPARE add_sale_fee_config_stmt FROM @add_sale_fee_config_sql;
EXECUTE add_sale_fee_config_stmt;
DEALLOCATE PREPARE add_sale_fee_config_stmt;
