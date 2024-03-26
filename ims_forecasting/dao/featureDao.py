import mysql.connector
from mysql.connector import Error
import pandas as pd

class featureDao:
    def __init__(self):
        self.conn = mysql.connector.connect(
            host="localhost",
            # host="host.docker.internal",
            user="root",
            password="root",
            database="ims",
            port="3306"
        )
    
    def insertCompanyFeature(self, data_dict):
        if self.conn.is_connected():
            try:
                cursor = self.conn.cursor()

                columns = ', '.join(data_dict.keys())
                placeholders = ', '.join(['%s'] * len(data_dict))
                updates = ', '.join([f"{key} = VALUES({key})" for key in data_dict.keys()])
                
                sql = f"""
                INSERT INTO f_company ({columns})
                VALUES ({placeholders})
                ON DUPLICATE KEY UPDATE {updates};
                """

                cursor.execute(sql, list(data_dict.values()))
                self.conn.commit()
            except Error as e:
                print(f"Error: {e}")
            finally:
                cursor.close()

    
    def findAllCompanyFeatures(self):
        if self.conn.is_connected():
            try:
                query = "SELECT * FROM f_company"
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
    
    def close(self):
        if self.conn:
            self.conn.close()
            
    def insertSalesFeature(self, df, table):
        if self.conn.is_connected():
            try:
                columns = ', '.join(df.columns)
           
                placeholders = ', '.join(['%s'] * len(df.columns))
             
                columns = ', '.join(df.columns)
                placeholders = ', '.join(['%s'] * len(df.columns))
                updates = ', '.join([f"{col} = VALUES({col})" for col in df.columns])
                
                sql = f"""
                INSERT INTO {table} ({columns})
                VALUES ({placeholders})
                ON DUPLICATE KEY UPDATE {updates};
                """
                cursor = self.conn.cursor()
                for _, row in df.iterrows():
                    cursor.execute(sql, tuple(row))

                self.conn.commit()
            except Error as e:
                print(f"Error: {e}")
            finally:
                cursor.close()
                
    def findSalesFeatureById(self, product_id, table):
        if self.conn.is_connected():
            try:
                query = f"SELECT * FROM {table} WHERE product_id = %s"
                cursor = self.conn.cursor()
                cursor.execute(query, (product_id,))
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []
        
    def findSalesFeatureByIds(self, product_ids, table):
        if self.conn.is_connected():
            try:
                placeholders = ', '.join(['%s'] * len(product_ids))  
                query = f"SELECT * FROM {table} WHERE product_id IN ({placeholders})"
                cursor = self.conn.cursor()
                
                cursor.execute(query, tuple(product_ids))
                columns = [col[0] for col in cursor.description]
                df = pd.DataFrame(cursor.fetchall(), columns=columns)
                return df
            except Error as e:
                print(f"Error: '{e}'")
            finally:
                cursor.close()
        return []
        