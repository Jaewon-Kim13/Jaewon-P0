import datetime
     
class Approvals:
    def __init__(self, id, expense_id, status, reviewer, comment):
        self.id = id
        self.expense_id = expense_id
        self.status = status
        self.reviewer = reviewer
        self.comment = comment
        self.date = datetime.datetime.now().date().isoformat()
        
def create_approval(conn, cursor, expense_id, status):
    try:
        cursor.execute("""
            INSERT INTO approval (expense_id, status)
            VALUES(?,?,?,?)
        """, (expense_id, status,))
        conn.commit()
        print("Successfully added approval to the database:")
    except Exception as e:
        print("Failed to add approval to the database:", e)