import sqlite3
import user

def db_init():
    conn = sqlite3.connect("persist.db")
    cursor = conn.cursor()
    
    with open('schema.sql', 'r') as f:
        sql_script = f.read()
        cursor.executescript(sql_script)

    conn.commit()
    return conn, cursor

def main():
    conn, cursor = db_init()
    
    user_id = 0
    while(True):
        print("1.)Create new user")
        print("2.)Create new user")
        choice = input("Enter selection: ")
        match choice:
            case "1":
                user.create_user()
            case "2":
                user.login()
            
    
if __name__ == "__main__":
    main()