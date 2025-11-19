import datetime
import approvals
import logging

logger = logging.getLogger(__name__)

class Expense:
    def __init__(self, id, user_id, amount, description, date=None):
        self.id = id
        self.user_id = user_id
        self.date = date if date else datetime.datetime.now().date().isoformat()
        self.amount = float(amount)
        self.description = description
    
    def validate(self):
        if self.amount < 0:
            return False
        if self.description is None or self.description:
            return False
        return True
    
    def __str__(self):
        return f"Expense(ID: {self.id}, User: {self.user_id}, Amount: ${self.amount:.2f}, Description: '{self.description}', Date: {self.date})"
    
    
#creates expense and corresponding approval!   
def create_expense(conn, cursor, my_id):
    logger.info(f"User(id:{my_id}): Creating new expense")
    user_id = my_id
    amount = input("Enter amount:")
    description = input("Enter description:")
    expense = Expense(None, user_id, amount, description)

    try:
        if expense.validate():
            raise ValueError()        
        
        cursor.execute("""
            INSERT INTO expenses (expense_date, amount, expense_description, user_id)
            VALUES(?,?,?,?)
        """, (expense.date, float(expense.amount), expense.description, expense.user_id))
        conn.commit()
        
        
        new_expense_id = cursor.lastrowid
        approvals.create_approval(conn, cursor, new_expense_id, "pending")
        print("Successfully added new expense to the database:")
        logger.info(f"User(id:{my_id}): Successfully Created new expense")
    except Exception as e:
        print("Failed to add to the database:", e)
        logger.error(f"User(id:{my_id}): Failed to Create new expense")
        
#get expense by id
def get_expense_and_status(conn, cursor, id, my_id):
    logger.info(f"User(id:{my_id}): Getting expense(id:{id})")
    try:
        cursor.execute("""
            SELECT 
                e.id AS expense_id,
                e.user_id,
                e.amount,
                e.expense_description,
                e.expense_date,
                a.approval_status
            FROM expenses e
            INNER JOIN approvals a ON e.id = a.expense_id
            WHERE e.id = ?;
        """, (id,))
        result = cursor.fetchone()
        expense = Expense(result[0], result[1], result[2], result[3], result[4])
        status = result[5]
        logger.info(f"User(id:{my_id}): Successfully got expense(id:{id})")
        return expense, status
    except Exception as e:
        print("Failed to fetch from the database:", e)
        logger.error(f"User(id:{my_id}): Failed to get expense(id:{id})")
        return None, None

#views all users expense and their status
def view_expense_and_status_by_user_id(conn, cursor, user_id):
    logger.info(f"User(id:{user_id}): Viewing their expenses")
    try:
        cursor.execute("""
            SELECT 
                e.id AS expense_id,
                e.user_id,
                e.amount,
                e.expense_description,
                e.expense_date,
                a.approval_status
            FROM expenses e
            INNER JOIN approvals a ON e.id = a.expense_id
            WHERE e.user_id = ?;
        """, (user_id,))
        
        results = cursor.fetchall()
        
        if not results:
            print(f"No expenses found for user {user_id}")
            return
       
        for row in results:
            print(f"Expense ID: {row[0]} Amount: ${row[2]:.2f} Description: {row[3]} Date: {row[4]} Status: {row[5]}")
        logger.info(f"User(id:{user_id}): Successfully Viewed their expenses")
    except Exception as e:
        print("Failed to fetch from the database:", e)
        logger.error(f"User(id:{user_id}): Failed to View their expenses")

def update_pending_or_denied_expense(conn, cursor, my_id, id):
    logger.info(f"User(id:{my_id}): Updating expense(id:{id})")
    my_expense, status = get_expense_and_status(conn, cursor, id, my_id)
    if status == "approved":
        print("Cannot edit approved expenses!")
        return
    if(my_expense == None):
        print("Expense Doesn't Not Exist For User")
        return
    
    print("Current expense: ", my_expense)
    user_id = my_id
    amount = input("Update amount:")
    description = input("Updated description:")
    expense = Expense(id, user_id, amount, description)
    try:
        if expense.validate():
            raise ValueError() 
        cursor.execute('''
            UPDATE expenses
            SET expense_date = ?, amount = ?, expense_description = ?
            WHERE id = ?
        ''', (expense.date, float(expense.amount), expense.description, expense.id))
        conn.commit()
        logger.info(f"User(id:{my_id}): Successfully Updated expense(id:{id})")
        print("Successfully updated expense(id):",id)
    except Exception as e:
        print(f"Failed to update expense({expense.id}) from the database:", e)
        logger.error(f"User(id:{my_id}): Failed to Update expense(id:{id})")

def delete_pending_or_denied_expense(conn, cursor, id, my_id):
    logger.info(f"User(id:{my_id}): Deleting expense(id:{id})")
    try:
        cursor.execute('''
            DELETE FROM expenses
            WHERE id = ? AND user_id = ?
        ''', (id, my_id))
        conn.commit()
        logger.info(f"User(id:{my_id}): Successfully Deleted expense(id:{id})")
        print("Successfully deleted expense(id):",id)
    except Exception as e:
        print(f"Failed to delete expense({id}) from the database:", e)
        logger.error(f"User(id:{my_id}): Failed to Delete expense(id:{id})")