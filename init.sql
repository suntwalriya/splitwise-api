--CREATE DATABASE IF NOT EXISTS splitwise_db;
--
--USE splitwise_db;
--
--CREATE TABLE splitwise_db.users (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    name VARCHAR(100) NOT NULL,
--    email_id VARCHAR(100) NOT NULL UNIQUE,
--    contact_number VARCHAR(15) NOT NULL UNIQUE,
--    username VARCHAR(50) NOT NULL UNIQUE,
--    password VARCHAR(255) NOT NULL,
--    status VARCHAR(20) NOT NULL DEFAULT 'active',
--    is_active BOOLEAN NOT NULL DEFAULT false,
--    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--);
--
--CREATE TABLE splitwise_db.user_balances (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    user_id INT NOT NULL,
--    friend_id INT NOT NULL,
--    balance DOUBLE NOT NULL,
--    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--    FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE,
--    UNIQUE KEY unique_user_friend (user_id, friend_id)
--);
--
--CREATE TABLE splitwise_db.user_groups (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    name VARCHAR(100) NOT NULL,
--    description VARCHAR(255),
--    created_by INT NOT NULL,
--    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--);
--
--
--CREATE TABLE splitwise_db.user_group_mapping (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    user_id INT NOT NULL,
--    group_id INT NOT NULL,
--    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--    FOREIGN KEY (group_id) REFERENCES user_groups(id) ON DELETE CASCADE
--);
--
--
--CREATE TABLE splitwise_db.expenses (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    description VARCHAR(255) NOT NULL,
--    amount DOUBLE NOT NULL,
--    paid_by INT NOT NULL,
--    created_by INT NOT NULL,
--    group_id INT NULL,
--    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
--    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    FOREIGN KEY (paid_by) REFERENCES users(id) ON DELETE CASCADE,
--    FOREIGN KEY (group_id) REFERENCES user_groups(id) ON DELETE CASCADE,
--    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
--);
--
--CREATE TABLE splitwise_db.expense_splits (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    expense_id INT NOT NULL,
--    user_id INT NOT NULL,
--    amount DOUBLE NOT NULL,
--    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
--    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
--);
--
--CREATE TABLE user_group_balances (
--    id INT AUTO_INCREMENT PRIMARY KEY,
--    user_id INT NOT NULL,
--    group_id INT NOT NULL,
--    balance DOUBLE NOT NULL,
--    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
--    FOREIGN KEY (group_id) REFERENCES user_groups(id) ON DELETE CASCADE
--);

INSERT INTO splitwise_db.users
(id, name, email_id, contact_number, username, password, status, is_active, created_at, updated_at)
VALUES(1, 'Bhaisi', 'Bhaisi@example.com', '13456781', 'Bhaisi', '$2a$10$gAziOWT1K4C4yzjRSKmmW.OUyrJmCNWTmTqm5sDxRmLfshdWAp4D6', 'active', 1, '2024-08-31 13:25:29', '2024-08-31 13:26:09');
INSERT INTO splitwise_db.users
(id, name, email_id, contact_number, username, password, status, is_active, created_at, updated_at)
VALUES(2, 'Vineeth', 'vineeth@example.com', '13456783', 'Vineeth', '$2a$10$E7Phzokxkx1j1XfRHy6S.e40uM8Ki8t/ybaeYsK8bLajdQPQ/xFZq', 'active', 1, '2024-08-31 13:25:48', '2024-08-31 13:26:21');
INSERT INTO splitwise_db.users
(id, name, email_id, contact_number, username, password, status, is_active, created_at, updated_at)
VALUES(3, 'Riya', 'Riya@example.com', '13456784', 'Riya', '$2a$10$BxjwXpBqrbJLejNxXHi6eey9/dn0angZ2WYWpgwFEaPVwvynIuk0K', 'active', 1, '2024-08-31 13:26:03', '2024-08-31 13:26:15');


INSERT INTO splitwise_db.user_groups
(id, name, description, created_by, created_at, updated_at)
VALUES(4, 'Coffee Fireside', 'A coffee trip with friends', 1, '2024-08-31 13:26:46', '2024-08-31 13:26:46');
INSERT INTO splitwise_db.user_groups
(id, name, description, created_by, created_at, updated_at)
VALUES(5, 'Chai Fireside', 'A chai trip with friends', 1, '2024-08-31 13:28:35', '2024-08-31 13:28:35');


INSERT INTO splitwise_db.user_group_mapping
(id, user_id, group_id, created_at, updated_at)
VALUES(2, 1, 4, '2024-08-31 13:26:46', '2024-08-31 13:26:46');
INSERT INTO splitwise_db.user_group_mapping
(id, user_id, group_id, created_at, updated_at)
VALUES(3, 2, 4, '2024-08-31 13:26:46', '2024-08-31 13:26:46');
INSERT INTO splitwise_db.user_group_mapping
(id, user_id, group_id, created_at, updated_at)
VALUES(4, 3, 4, '2024-08-31 13:26:46', '2024-08-31 13:26:46');
INSERT INTO splitwise_db.user_group_mapping
(id, user_id, group_id, created_at, updated_at)
VALUES(5, 1, 5, '2024-08-31 13:28:35', '2024-08-31 13:28:35');
INSERT INTO splitwise_db.user_group_mapping
(id, user_id, group_id, created_at, updated_at)
VALUES(6, 2, 5, '2024-08-31 13:28:35', '2024-08-31 13:28:35');


INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(2, 'Dinner at Japan Restaurant', 300.0, 1, 1, 4, '2024-08-31 14:10:23', '2024-08-31 14:10:23');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(3, 'Dinner at Japan Restaurant', 400.0, 2, 2, 4, '2024-08-31 14:11:29', '2024-08-31 14:11:29');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(4, 'Dinner at Japan Restaurant', 400.0, 2, 2, 4, '2024-08-31 14:29:30', '2024-08-31 14:29:30');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(5, 'Dinner at Japan Restaurant', 1000.0, 2, 2, 4, '2024-08-31 14:30:59', '2024-08-31 14:30:59');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(6, 'Dinner at Japan Restaurant', 1000.0, 2, 2, 4, '2024-08-31 14:41:52', '2024-08-31 14:41:52');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(7, 'Dinner at Japan Restaurant', 1000.0, 2, 2, 4, '2024-08-31 14:43:16', '2024-08-31 14:43:16');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(8, 'Dinner at Japan Restaurant', 1000.0, 1, 1, 4, '2024-08-31 14:44:10', '2024-08-31 14:44:10');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(9, 'Dinner at Japan Restaurant', 1000.0, 1, 1, 4, '2024-08-31 14:52:25', '2024-08-31 14:52:25');
INSERT INTO splitwise_db.expenses
(id, description, amount, paid_by, created_by, group_id, created_at, updated_at)
VALUES(10, 'Dinner at Japan Restaurant', 700.0, 2, 2, 4, '2024-08-31 14:54:34', '2024-08-31 14:54:34');



INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(1, 2, 1, 100.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(2, 2, 2, 100.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(3, 2, 3, 100.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(4, 3, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(5, 4, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(6, 4, 2, 0.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(7, 4, 3, 100.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(8, 5, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(9, 5, 2, 0.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(10, 5, 3, 700.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(11, 6, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(12, 6, 2, 200.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(13, 6, 3, 500.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(14, 7, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(15, 7, 2, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(16, 7, 3, 400.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(17, 8, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(18, 8, 2, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(19, 8, 3, 400.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(20, 9, 1, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(21, 9, 2, 300.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(22, 9, 3, 400.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(23, 10, 1, 200.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(24, 10, 2, 100.0);
INSERT INTO splitwise_db.expense_splits
(id, expense_id, user_id, amount)
VALUES(25, 10, 3, 400.0);



INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(1, 1, 2, 100.0, '2024-08-31 14:52:25', '2024-08-31 14:54:34');
INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(2, 2, 1, -100.0, '2024-08-31 14:52:25', '2024-08-31 14:54:34');
INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(3, 1, 3, 400.0, '2024-08-31 14:52:25', '2024-08-31 14:52:25');
INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(4, 3, 1, -400.0, '2024-08-31 14:52:25', '2024-08-31 14:52:25');
INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(5, 2, 3, 400.0, '2024-08-31 14:54:34', '2024-08-31 14:54:34');
INSERT INTO splitwise_db.user_balances
(id, user_id, friend_id, balance, created_at, updated_at)
VALUES(6, 3, 2, -400.0, '2024-08-31 14:54:34', '2024-08-31 14:54:34');


INSERT INTO splitwise_db.user_group_balances
(id, user_id, group_id, balance, created_at, updated_at)
VALUES(13, 1, 4, 500.0, '2024-08-31 14:52:25', '2024-08-31 14:54:35');
INSERT INTO splitwise_db.user_group_balances
(id, user_id, group_id, balance, created_at, updated_at)
VALUES(14, 2, 4, 300.0, '2024-08-31 14:52:25', '2024-08-31 14:54:34');
INSERT INTO splitwise_db.user_group_balances
(id, user_id, group_id, balance, created_at, updated_at)
VALUES(15, 3, 4, -800.0, '2024-08-31 14:52:25', '2024-08-31 14:54:35');