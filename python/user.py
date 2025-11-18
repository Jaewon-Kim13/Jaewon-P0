import sqlite3
import logging

logger = logging.getLogger(__name__)

class User:
    def __init__(self, id, username, password, role):
        self.id = id
        self.username = username
        self.password = password
        self.role = role

def create_user(conn, cursor):
    logger.info("Creating new user")
    print("Creating user:")
    username = input("Enter username: ")
    password = input("Enter password: ")
    try:
        cursor.execute("""
            INSERT INTO users (username, user_password, user_role)
            VALUES(?,?,?)
        """, (username, password, 'Employee'))
        conn.commit()
        print("Successfully added new User to the database:")
        user_id = cursor.lastrowid
        logger.info(f"Successfully created new User(id:{user_id})")
        return user_id
    except Exception as e:
        print("User Creation failed: ",e)
        logger.error(f"Failed to create new user: {e}")
        return -1
       
def login(conn, cursor):
    logger.info("User attempting to login")
    print("Login user:")
    username = input("Enter username: ")
    password = input("Enter password: ")
    
    try:
        cursor.execute("""
            Select id FROM users WHERE username=? AND user_password=?
        """, (username, password))
        conn.commit()
        result = cursor.fetchone()[0]
        print("Successfully Logged in:")
        logger.info(f"User(id:{result}): Successfully logged in")
        return result
    except Exception as e:
        print("User Login failed:",e)
        logger.error("Failed to login user")
        return -1