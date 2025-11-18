import sqlite3
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
    conn = sqlite3.connect("persist.db")
    cursor = conn.cursor()
    
    with open('/home/user/rev/p0/python/schema.sql', 'r') as f:
        sql_script = f.read()
        cursor.executescript(sql_script)

    conn.commit()
    return conn, cursor

def main():
    conn, cursor = db_init()
    user_id = -1
    while(True):
        print("----------Menu----------")
        print("Welcome to User Expense Manager!")
        print("1.)Create new user")
        print("2.)Login new user")
        print("3.)Exit")
        print("------------------------\n")
        choice = input("Enter selection: ")
        match choice:
            case "1":
                print("\n++++++++++++++++++++")
                user_id = user.create_user(conn, cursor)
                print("++++++++++++++++++++")
            case "2":
                print("\n++++++++++++++++++++")
                user_id = user.login(conn, cursor)
                print("++++++++++++++++++++")
            case "3":
                sys.exit(0)
            case _:
                print("Invalid Choice")
        
        if(user_id == -1): continue
        else:break
        
    while(True):
        print("----------Menu----------")
        print("1.)Create Expense")
        print("2.)Get Expense by ID")
        print("3.)View all my expenses")
        print("4.)Update pending/denied expenses")
        print("5.)Delete pending/denied expenses")
        print("6.)Exit")
        print("------------------------\n")
        choice = input("Enter Menu Option:")
        match choice:
            case "1":
                print("\n++++++++++++++++++++")
                expense.create_expense(conn, cursor, user_id)
                print("++++++++++++++++++++")
            case "2":
                print("\n++++++++++++++++++++")
                id = int(input("Enter expense id:"))
                my_expense, status = expense.get_expense_and_status(conn, cursor,id, user_id)
                print(my_expense, status)
                print("++++++++++++++++++++")
            case "3":
                print("\n++++++++++++++++++++")
                expense.view_expense_and_status_by_user_id(conn, cursor, user_id)
                print("++++++++++++++++++++")
            case "4":
                print("\n++++++++++++++++++++")
                id = int(input("Enter expense id:"))
                expense.update_pending_or_denied_expense(conn, cursor, user_id, id)
                print("++++++++++++++++++++")
            case "5":
                print("\n++++++++++++++++++++")
                id = int(input("Enter expense id:"))
                expense.delete_pending_or_denied_expense(conn, cursor, id, user_id)
                print("++++++++++++++++++++")
            case "6":
                sys.exit(0)
            case _:
                print("\n++++++++++++++++++++")
                print("Invalid Choice")
                print("++++++++++++++++++++")
        
if __name__ == "__main__":
    main()