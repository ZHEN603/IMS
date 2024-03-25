import mysql.connector
from mysql.connector import Error
from datetime import datetime

class companyDao:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host="localhost",
            # host="host.docker.internal",
            user="root",
            password="root",
            database="ims",
            port="3306"
        )
    
    def findNeedFeatrueCompany(self):
        if self.conn.is_connected():
            try:
                now = datetime.now()
                last_month = now
                last_month = last_month.strftime('%Y-%m-%d %H:%M:%S')
                query = "SELECT id,address,name,type FROM c_company WHERE update_time <= %s or update_time is null"
                cursor = self.conn.cursor()
                cursor.execute(query, (last_month,))
                companies = cursor.fetchall()
                return companies
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []


    def findAllCompany(self):
        if self.conn.is_connected():
            try:
                query = "SELECT id FROM c_company"
                cursor = self.conn.cursor()
                cursor.execute(query)
                company_ids = cursor.fetchall()
                return company_ids
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []
    
    def insertCompanyFeature(self, data_dict):
        if self.conn.is_connected():
            try:
                cursor = self.conn.cursor()

                columns = ', '.join(data_dict.keys())
                placeholders = ', '.join(['%s'] * len(data_dict))
                sql = f"INSERT INTO f_company ({columns}) VALUES ({placeholders})"

                cursor.execute(sql, list(data_dict.values()))
                self.conn.commit()
            except Error as e:
                print(f"Error: {e}")
            finally:
                cursor.close()

    
    
    def close(self):
        if self.conn:
            self.conn.close()