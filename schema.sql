-- Active: 1764822103861@@127.0.0.1@3306@database
-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    user_password VARCHAR(255) NOT NULL,
    user_role ENUM('Employee', 'Manager') NOT NULL
);

CREATE TABLE IF NOT EXISTS expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL CHECK(amount > 0),
    expense_description TEXT NOT NULL,
    expense_date DATE NOT NULL,
    category VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS approvals (
    id INT PRIMARY KEY AUTO_INCREMENT,
    expense_id INT NOT NULL UNIQUE,
    approval_status ENUM('pending', 'approved', 'denied') NOT NULL DEFAULT 'pending',
    reviewer INT,
    comment TEXT,
    review_date DATETIME,
    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer) REFERENCES users(id) ON DELETE SET NULL
);

SELECT 
    CONCAT('KILL ', ID, ';') AS kill_command
FROM 
    INFORMATION_SCHEMA.PROCESSLIST
WHERE 
    DB = 'database'
    AND ID != CONNECTION_ID();