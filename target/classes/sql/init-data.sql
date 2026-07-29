USE bookkeeping;

INSERT INTO sys_user (
  username,
  password,
  nickname,
  phone,
  status,
  created_at,
  updated_at,
  deleted
) VALUES (
  'admin',
  '$2b$10$nY/7tBQHcHmxKW.sx2okd.6o0fEXLdqVH/JAlcFidZ4mILxi8gngG',
  '管理员',
  NULL,
  1,
  NOW(),
  NOW(),
  0
) ON DUPLICATE KEY UPDATE
  nickname = VALUES(nickname),
  status = VALUES(status),
  updated_at = NOW();
