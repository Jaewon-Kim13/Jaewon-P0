import mysql.connector
import sys
import user
import expense
import logging

logging.basicConfig(
    filename='app.log',
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)

def db_init():
    conn = mysql.connector.connect(
        host="localhost",
        port=3306,
        user="user",
        password="userpassword",
        database="database"
    )
    cursor = conn.cursor()
    
    # with open('/home/user/rev/p0/schema.sql', 'r') as f:
    #     sql_script = f.read()
    #     # Split by semicolons and execute each statement separately
    #     statements = sql_script.split(';')
    #     for statement in statements:
    #         statement = statement.strip()
    #         if statement:  # Skip empty statements
    #             cursor.execute(statement)

    conn.commit()
    return conn, cursor

def main():
    conn, cursor = db_init()
    user_id = -1
    while(True):
        print(f"\n{'='*40}")
        print("Welcome to User Expense Manager!")
        print(f"{'='*40}")
        print("1.) Create new user")
        print("2.) Login new user")
        print("3.) Exit")
        print(f"{'='*40}\n")
        choice = input("Enter selection: ")
        match choice:
            case "1":
                print()
                user_id = user.create_user(conn, cursor)
            case "2":
                print()
                user_id = user.login(conn, cursor)
            case "3":
                if cursor:
                    cursor.close()
                if conn:
                    conn.close()
                sys.exit(0)
            case _:
                print("Invalid choice")
        
        if(user_id == -1): continue
        else: break
        
    while(True):
        print(f"\n{'='*40}")
        print("Expense Management Menu")
        print(f"{'='*40}")
        print("1.) Create expense")
        print("2.) Get expense by ID")
        print("3.) View all my expenses")
        print("4.) Update pending expenses")
        print("5.) Delete pending expenses")
        print("6.) Exit")
        print(f"{'='*40}\n")
        choice = input("Enter menu option: ")
        match choice:
            case "1":
                print()
                expense.create_expense(conn, cursor, user_id)
            case "2":
                try:
                    print()
                    id = int(input("Enter expense ID: "))
                    my_expense, status = expense.get_expense_and_status(conn, cursor, id, user_id)
                    print(f"\n{my_expense}")
                    print(f"Status: {status}")
                except Exception as e:
                    print("Enter Valid input!")
            case "3":
                print()
                expense.view_expense_and_status_by_user_id(conn, cursor, user_id)
            case "4":
                try:
                    print()
                    id = int(input("Enter expense ID: "))
                    expense.update_pending_or_denied_expense(conn, cursor, user_id, id)
                except Exception as e:
                    print("Enter Valid input!")
                
            case "5":
                try:
                    print()
                    id = int(input("Enter expense ID: "))
                    expense.delete_pending_or_denied_expense(conn, cursor, id, user_id)
                except Exception as e:
                    print("Enter Valid input!")
            case "6":
                if cursor:
                    cursor.close()
                if conn:
                    conn.close()
                sys.exit(0)
            case _:
                print("\nInvalid choice")
        
if __name__ == "__main__":
    main()