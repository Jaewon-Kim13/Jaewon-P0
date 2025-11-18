import datetime
import logging

logger = logging.getLogger(__name__)
     
class Approvals:
    def __init__(self, id, expense_id, status, reviewer, comment):
        self.id = id
        self.expense_id = expense_id
        self.status = status
        self.reviewer = reviewer
        self.comment = comment
        self.date = datetime.datetime.now().date().isoformat()
        
def create_approval(conn, cursor, expense_id, status):
    logger.info(f"Creating new Approval for Expense(id:{expense_id})")
    try:
        cursor.execute("""
            INSERT INTO approvals (expense_id, approval_status)
            VALUES(?,?)
        """, (expense_id, status))
        conn.commit()
        logger.info(f"Successfully Creating new Approval for Expense(id:{expense_id})")
        print("Successfully added approval to the database:")
    except Exception as e:
        print("Failed to add approval to the database:", e)
        logger.info(f"Failed to Create new Approval for Expense(id:{expense_id})")