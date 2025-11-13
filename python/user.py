import sqlite3

class User:
    def __init__(self, id, username, password, role):
        self.id = id
        self.username = username
        self.password = password
        self.role = role

def create_user(conn, cursor):
    print("Creating user:")
    username = input("Enter username: ")
    password = input("Enter password: ")
    
    try:
        cursor.execute("""
            INSERT INTO users (username, password, role)
            VALUES(?,?,?,?)
        """, (username, password, 'Employee'))
        conn.commit()
        print("Successfully added new User to the database:") 
    except:
        print("User Creation failed: ")
       
def login(conn, cursor):
    print("Login user:")
    username = input("Enter username: ")
    password = input("Enter password: ")
    
    try:
        cursor.execute("""
            Select id FROM users WHERE username=? AND password=?
        """, (username, password))
        conn.commit()
        print("Successfully added new User to the database:") 
    except:
        print("User Creation failed: ")