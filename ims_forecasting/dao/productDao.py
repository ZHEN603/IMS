import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta
import pandas as pd

class productDao:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host="localhost",
            # host="host.docker.internal",
            user="root",
            password="root",
            database="ims",
            port="3306"
        )
    

    def findNewProduct(self,company_id):
        if self.conn.is_connected():
            try:
                now = datetime.now()
                yesterday = now - timedelta(days=1)
                yesterday_midnight = datetime(yesterday.year, yesterday.month, yesterday.day, 0, 0, 0)
                yesterday_midnight = yesterday_midnight.strftime('%Y-%m-%d %H:%M:%S')
                query = "SELECT id FROM pro_product WHERE create_time >= %s AND company_id = %s"
                cursor = self.conn.cursor()
                cursor.execute(query, (yesterday_midnight,company_id))
                return cursor.fetchall()
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []

    def findProductByCompanyId(self,company_id):
        if self.conn.is_connected():
            try:
                query = "SELECT * FROM pro_product WHERE company_id = %s"
                cursor = self.conn.cursor()
                cursor.execute(query, (company_id,))
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []
    
    def findAllProduct(self):
        if self.conn.is_connected():
            try:
                query = "SELECT * FROM pro_product"
                cursor = self.conn.cursor()
                cursor.execute(query)
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []

