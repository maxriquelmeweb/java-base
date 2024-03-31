INSERT INTO users (active, created_at, updated_at, lastname, name, email, password)
VALUES (true, NOW(), NOW(), 'xxxx', 'xxxx', 'xxxxx@xxxx.com', 'passwordhasheado');
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);
