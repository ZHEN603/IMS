import mysql.connector
from mysql.connector import Error
from datetime import datetime, timedelta
import pandas as pd

class orderDao:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host="localhost",
            # host="host.docker.internal",
            user="root",
            password="root",
            database="ims",
            port="3306"
        )
    

    def findUpdateHistorySales(self, company_id):
        if self.conn.is_connected():
            try:
                now = datetime.now()
                first_day_of_month = datetime(now.year, now.month, 1)
                first_day_of_month = first_day_of_month.strftime('%Y-%m-%d %H:%M:%S')
                
                yesterday = now - timedelta(days=1)
                yesterday_midnight = datetime(yesterday.year, yesterday.month, yesterday.day, 0, 0, 0)
                yesterday_midnight = yesterday_midnight.strftime('%Y-%m-%d %H:%M:%S')
                query = "SELECT id FROM in_order_detail WHERE complete_time < %s AND update_time >= %s AND company_id = %s AND type = 0"
                cursor = self.conn.cursor()
                cursor.execute(query, (first_day_of_month,yesterday_midnight,company_id))
                return cursor.fetchall()
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []

    def findNewOrder(self, company_id):
        if self.conn.is_connected():
            try:
                now = datetime.now()
                first_day_of_month = datetime(now.year, now.month, 1)
                first_day_of_month = first_day_of_month.strftime('%Y-%m-%d %H:%M:%S')

                query = "SELECT * FROM in_order_detail WHERE complete_time < %s AND company_id = %s AND type = 0"
                cursor = self.conn.cursor()
                cursor.execute(query, (first_day_of_month,company_id))
                return cursor.fetchall()
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []

    def findAllOrderByCompanyId(self, company_id):
        if self.conn.is_connected():
            try:
                now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
                query = "SELECT * FROM in_order_detail WHERE company_id = %s AND complete_time <= %s AND type = 0 ORDER BY complete_time ASC"
                cursor = self.conn.cursor()
                cursor.execute(query, (company_id,now))
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []
    
    def findAllOrder(self):
        if self.conn.is_connected():
            try:
                now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')

                query = "SELECT * FROM in_order_detail WHERE complete_time <= %s AND type = 0 ORDER BY complete_time ASC"
                cursor = self.conn.cursor()
                cursor.execute(query, (now,))
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []