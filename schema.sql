-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    user_password TEXT NOT NULL,
    user_role TEXT NOT NULL CHECK(user_role IN ('Employee', 'Manager'))
);

-- Expenses table
CREATE TABLE IF NOT EXISTS expenses (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    amount REAL NOT NULL CHECK(amount > 0),
    expense_description TEXT NOT NULL,
    expense_date TEXT NOT NULL,
    category TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Approvals table
CREATE TABLE IF NOT EXISTS approvals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    expense_id INTEGER NOT NULL UNIQUE,
    approval_status TEXT NOT NULL DEFAULT 'pending' CHECK(approval_status IN ('pending', 'approved', 'denied')),
    reviewer INTEGER,
    comment TEXT,
    review_date TEXT,
    FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer) REFERENCES users(id) ON DELETE SET NULL
);