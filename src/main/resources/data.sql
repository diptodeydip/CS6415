INSERT INTO `users` (id, `created_at`, email, password, first_name, last_name, role, verified)
SELECT 1, CURRENT_TIMESTAMP, 'dipdey093@gmail.com', '$2b$12$cVmV3NHc.ooMEgYTuaaGjuJJeBvOU1g4pMERdZAPrJRF61yYvOpKq', 'super', 'user', 'ADMIN', true
    WHERE NOT EXISTS (SELECT * FROM `users` WHERE `email`='dipdey093@gmail.com');

INSERT INTO `roles` (id, name, permissions)
SELECT 1, 'ADMIN', 'ROLE_ADMIN'
    WHERE NOT EXISTS (SELECT * FROM `roles` WHERE `name`='ADMIN');