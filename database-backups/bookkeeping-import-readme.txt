bookkeeping 数据库导入说明

最新完整备份：bookkeeping-all-data-with-schema-20260807-011842.sql
完整备份压缩包：bookkeeping-all-data-with-schema-20260807-011842.sql.zip
仅数据备份：bookkeeping-business-data-only-20260807-011842.sql

完整覆盖导入：
mysql -uroot -p -e "DROP DATABASE IF EXISTS bookkeeping;"
mysql --default-character-set=utf8mb4 -uroot -p < "bookkeeping-all-data-with-schema-20260807-011842.sql"

仅覆盖表内数据（要求目标库已有相同表结构）：
mysql --default-character-set=utf8mb4 -uroot -p bookkeeping < "bookkeeping-business-data-only-20260807-011842.sql"

说明：完整备份包含 CREATE DATABASE、全部 15 张表的结构和全部数据，共 1061 行数据。
