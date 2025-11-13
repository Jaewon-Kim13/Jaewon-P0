import datetime
import approvals

class Expense:
    def __init__(self, id, user_id, amount, category, description):
        self.id = id
        self.user_id = user_id
        self.date = datetime.datetime.now().date().isoformat()
        self.amount = amount
        self.category = category
        self.description = description
        
    def __init__(self, id, user_id, amount, category, description, date):
        self.id = id
        self.user_id = user_id
        self.date = date
        self.amount = amount
        self.category = category
        self.description = description
    
    def validate(self):
        if self.amount < 0:
            return False
        return True
    
#creates expense and corresponging approval!   
def create_expense(conn, cursor, my_id):
    user_id = my_id
    amount = input("Enter amount:")
    category = input("Enter category:")
    description = input("Enter description:")
    expense = Expense(None, user_id, amount, category, description)
    
    try:
        cursor.execute("""
            INSERT INTO expenses (date, amount, category, description, user_id)
            VALUES(?,?,?,?)
        """, (expense.date, expense.amount, expense.category, expense.description, expense.user_id))
        conn.commit()
        print("Successfully added new expense to the database:")
        
        new_expense_id = cursor.lastrowid
        approvals.create_approval(conn, cursor, new_expense_id, "pending")
    except Exception as e:
        print("Failed to add to the database:", e)
        
#get expense by id
def get_expense_and_status(conn, cursor, id):
    try:
        cursor.execute("""
            SELECT 
                e.id AS expense_id,
                e.user_id,
                e.amount,
                e.category,
                e.description,
                e.date,
                a.status,
            FROM expenses e
            INNER JOIN approvals a ON e.id = a.expense_id
            WHERE e.id = ?;
        """, (id,))
        result = cursor.fetchone()
        expense = Expense(result[0],result[1], result[2], result[3], result[5], result[6])
        status = result[6]
        return expense, status
    except Exception as e:
        print("Failed to fetch from the database:", e)
        return None 

#views all users expense and their status
def view_expense_and_status_by_user_id(conn, cursor, user_id):
    try:
        cursor.execute("""
            SELECT 
                e.id AS expense_id,
                e.user_id,
                e.amount,
                e.category,
                e.description,
                e.date,
                a.status,
            FROM expenses e
            INNER JOIN approvals a ON e.id = a.expense_id
            WHERE e.user_id = ?;
        """, (user_id,))
        
        results = cursor.fetchall()
        
        if not results:
            print(f"No expenses found for user {user_id}")
            return
       
        for row in results:
            print(f"Expense ID: {row[0]} Amount: ${row[2]:.2f} Category: {row[3]} Description: {row[4]} Date: {row[5]} Status: {row[6]}")
        
    except Exception as e:
        print("Failed to fetch from the database:", e) 

def update_pending_or_denied_expense(conn, cursor, my_id, id):
    my_expense, status = get_expense_and_status(conn, cursor, id)
    if(status =="approved"):
        print("Cannot edit approved expenses!")
        return
    
    user_id = my_id
    amount = input("Enter amount:")
    category = input("Enter category:")
    description = input("Enter description:")
    expense = Expense(id, user_id, amount, category, description)
    try:
        cursor.execute('''
            UPDATE expenses
            SET date = ?, amount = ?, category = ?, description = ?
            WHERE id = ?
        ''', (expense.date, expense.amount, expense.category, expense.description, expense.id))
    except Exception as e:
        print(f"Failed to update expense({expense.id}) from the database:", e)

def delete_pending_or_denied_expense(conn, cursor, id):
    try:
        cursor.execute('''
            DELETE FROM expenses
            WHERE id = ?
        ''', (id,))
        conn.commit()
    except Exception as e:
        print(f"Failed to delete expense({id}) from the database:", e)