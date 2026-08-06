bookkeeping 数据库导入说明

SQL 文件：bookkeeping-full-20260806-230454.sql
压缩包：bookkeeping-full-20260806-230454.sql.zip

在家里电脑导入前，请先确认已经安装 MySQL，并且知道 root 密码。

方式一：直接导入 SQL
mysql --default-character-set=utf8mb4 -uroot -p < "bookkeeping-full-20260806-230454.sql"

方式二：如果当前已有 bookkeeping 库并希望完全覆盖
mysql -uroot -p -e "DROP DATABASE IF EXISTS bookkeeping;"
mysql --default-character-set=utf8mb4 -uroot -p < "bookkeeping-full-20260806-230454.sql"

说明：这个 SQL 使用 --databases 导出，里面包含 CREATE DATABASE / USE bookkeeping。
