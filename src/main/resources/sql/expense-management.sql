INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission_code, icon, sort_order, status, visible, remark, created_at, updated_at, deleted)
SELECT 26, 0, '经营支出', 'C', '/expenses', 'ExpenseManageView', 'expense:view', 'Money', 50, 1, 1, NULL, NOW(), NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE id = 26);

UPDATE sys_menu
SET menu_name = '经营支出',
    path = '/expenses',
    component = 'ExpenseManageView',
    permission_code = 'expense:view',
    icon = 'Money',
    sort_order = 50,
    status = 1,
    visible = 1,
    updated_at = NOW(),
    deleted = 0
WHERE id = 26;

INSERT IGNORE INTO sys_role_menu (role_id, menu_id, created_at)
SELECT 1, 26, NOW()
FROM sys_role
WHERE id = 1;
