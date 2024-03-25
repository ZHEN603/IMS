from services.modelService import modelService
import time
from datetime import datetime
from dao.companyDao import companyDao
from dao.orderDao import orderDao
from dao.productDao import productDao
from dao.featureDao import featureDao
import pandas as pd
import requests
import numpy as np
from itertools import product
from sklearn.preprocessing import LabelEncoder

class dataService:
    def __init__(self):
        self.companyDao = companyDao()
        self.orderDao = orderDao()
        self.productDao = productDao()
        self.featureDao = featureDao()
        self.modelService = modelService()
        self.types = ['cafe','restaurant','convenience_store','store','shopping_mall','supermarket','living','bus_station','subway_station','parking','school','museum','art_gallery','library','doctor','dentist','hospital','spa','night_club','park','bar','beauty_salon','fire_station','police','local_government_office']
        self.base_url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
        self.api_key = 'AIzaSyB66r29zI-MlSAxOyORDnYCGZgOceQvSUc'
        self.radius = ['100','200','300']
        self.params = {
            'location': '45.01274129999999,38.93038689999999',  
            'radius': '50',  
            'type': 'living',  
            'key': self.api_key
        }
        self.search_url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json"
        self.search_params = {
            'input': '',
            'inputtype': 'textquery',
            'fields': 'geometry',
            'key': self.api_key
        }

    
    def checkNeedUpdateSales(self, company_id):
        if len(self.orderDao.findUpdateHistorySales(company_id)) == 0:
            return False
        elif len(self.productDao.findNewProduct(company_id)) == 0:
                return False
        return True
    
    
    def salesRoutine(self):
        company_ids = self.companyDao.findAllCompany()
        now = datetime.now()
        flag= False
        for company_id in company_ids:
            company_id = company_id[0]
            print(company_id)
            if now.day == 1:
                if len(self.orderDao.findNewOrder(company_id)) == 0:
                    continue
            else:
                if self.checkNeedUpdateSales(company_id) == False:
                    continue
            flag = True
            self.updateSales(company_id)
            
        if flag:
            self.updateShopFeatureSales()
            
            
    def shopFeatureRoutine(self):
        companies = self.companyDao.findNeedFeatrueCompany()
        companies = pd.DataFrame(companies).rename(columns={0: 'id', 1: 'address', 2: 'name', 3:'type'})
        print(companies)
        for i,company in companies.iterrows():
            print(company)
            self.collectFeatureByCompany(company)

    
    def collectFeatureByCompany(self, company):
        address=str(company['name'])+','+str(company['address'])
        self.search_params['input'] = address
        response = requests.get(self.search_url, params=self.search_params)
        if len(response.json()['candidates']) == 0:
            return
        
        location = response.json()['candidates'][0]['geometry']['location']
        temp = {}
        temp['company_id']=company['id']
        temp['name']=company['name']
        temp['type']=company['type']
        for type in self.types:  
            print(type)
            self.params['location'] = str(location['lat'])+','+str(location['lng'])
            temp['location'] = self.params['location']
            self.params['type'] = type
            rating = 0
            people = 0
            for r in self.radius:
                cnt=0
                self.params['radius'] = r
                self.params['pagetoken'] = ''
                for i in range(3):
                    # print(params)
                    response = requests.get(self.base_url, params=self.params)
                    results = response.json()
                    cnt+=len(results['results'])
                    if r=='300':
                        for res in results['results']:
                            if 'rating' in res:
                            #   print(res['user_ratings_total'])
                                people += res['user_ratings_total']
                                rating += res['rating'] * res['user_ratings_total']
                    time.sleep(2)
                    if 'next_page_token' in results:
                        self.params['pagetoken'] = results['next_page_token']
                    else:
                        break
                temp[type+'_'+r] = cnt
            temp[type+'_rating'] = rating
            if people != 0:
                temp[type+'_rating'] /= people
            temp[type+'_user_ratings_total'] = people
            print(temp)
        self.featureDao.insertCompanyFeature(temp)
        
        
    def updateSales(self, company_id):
        df=self.orderDao.findAllOrderByCompanyId(company_id)
        # df=pd.read_csv('./shop_34.csv').drop(columns=['date_block_num'])
        df['product_id'] = df['product_id'].astype('int64')


        pro=self.productDao.findProductByCompanyId(company_id)
        # pro=pd.read_csv('./shop34_items.csv')[['product_id','category_id']]
        # pro['code']=pro['product_id']
        pro['category_id'] = pro['category_id'].astype('int64')
        pro['category_id'] = LabelEncoder().fit_transform(pro['category_id'])
        pro.rename(columns={'id': 'product_id'}, inplace=True)
        pro['product_id'] = pro['product_id'].astype('int64')

        

        df['date'] = pd.to_datetime(df['complete_time'])
        df['date_block_num'] = (df['date'].dt.year - df['date'].dt.year.min()) * 12 + (df['date'].dt.month - df['date'].dt.month.min())
        df['month'] = df['date'].dt.month
        df['days'] = df['date'] + pd.offsets.MonthEnd(0)
        df['days'] = df['days'].dt.day
        
        if df['date_block_num'].max()<4:
            return
        
        last_month = df['date_block_num'].max()
        
        last_month_sales = df[df['date_block_num'] == last_month]

        missing_products = pro[~pro['product_id'].isin(last_month_sales['product_id'])]
        
        missing_products['date_block_num'] = last_month
        missing_products['month'] = df[df['date_block_num'] == last_month]['month'].max()
        missing_products['days'] = df[df['date_block_num'] == last_month]['days'].max()
        print(missing_products)
        print(df)
        missing_products['quantity'] = 0
        missing_products['price'] = 100
        
        df = pd.concat([df, missing_products], ignore_index=True)



        ts = time.time()
        matrix = []
        cols = ['date_block_num','product_id']
        for i in range(df['date_block_num'].min(),df['date_block_num'].max()+1):
            sales = df[df.date_block_num==i]
            matrix.append(np.array(list(product([i], sales.product_id.unique()))))
            
        matrix = pd.DataFrame(np.vstack(matrix), columns=cols)
        matrix['date_block_num'] = matrix['date_block_num']
        matrix['product_id'] = matrix['product_id']
        matrix.sort_values(cols,inplace=True)
        time.time() - ts

        groupby = df.groupby(['product_id','date_block_num']).agg({'quantity':'sum'})
        groupby.columns = ['product_cnt_month']
        groupby.reset_index(inplace=True)
        groupby.columns = ['product_id', 'date_block_num', 'product_cnt_month']
        matrix = matrix.merge(groupby, on = ['product_id','date_block_num'], how = 'left')
        matrix['product_cnt_month'] = matrix['product_cnt_month'].fillna(0).clip(0,20).astype(np.float16)
        matrix = matrix.merge(pro[['product_id','category_id']], on = ['product_id'], how = 'left')
        
        def lag_feature(df, lags, col):
            tmp = df[['date_block_num','product_id',col]]
            for i in lags:
                shifted = tmp.copy()
                shifted.columns = ['date_block_num','product_id', col+'_lag_'+str(i)]
                shifted['date_block_num'] += i
                df = pd.merge(df, shifted, on=['date_block_num','product_id'], how='left')
            return df
        
        ts = time.time()
        matrix = lag_feature(matrix, [1,2,3,6,12], 'product_cnt_month')
        time.time() - ts
        
        ts = time.time()
        group = matrix.groupby(['date_block_num']).agg({'product_cnt_month': ['mean']})
        group.columns = [ 'date_avg_product_cnt' ]
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['date_block_num'], how='left')
        matrix['date_avg_product_cnt'] = matrix['date_avg_product_cnt'].astype(np.float16)
        matrix = lag_feature(matrix, [1,2,3,6,12], 'date_avg_product_cnt')
        matrix.drop(['date_avg_product_cnt'], axis=1, inplace=True)
        time.time() - ts
        
        ts = time.time()
        group = matrix.groupby(['date_block_num', 'product_id']).agg({'product_cnt_month': ['mean']})
        group.columns = [ 'date_product_avg_product_cnt' ]
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['date_block_num','product_id'], how='left')
        matrix['date_product_avg_product_cnt'] = matrix['date_product_avg_product_cnt'].astype(np.float16)
        matrix = lag_feature(matrix, [1,2,3,6,12], 'date_product_avg_product_cnt')
        matrix.drop(['date_product_avg_product_cnt'], axis=1, inplace=True)
        time.time() - ts
        
        ts = time.time()
        group = matrix.groupby(['date_block_num', 'category_id']).agg({'product_cnt_month': ['mean']})
        group.columns = [ 'date_cat_avg_product_cnt' ]
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['date_block_num','category_id'], how='left')
        matrix['date_cat_avg_product_cnt'] = matrix['date_cat_avg_product_cnt'].astype(np.float16)
        matrix = lag_feature(matrix, [1,2,3,6,12], 'date_cat_avg_product_cnt')
        matrix.drop(['date_cat_avg_product_cnt'], axis=1, inplace=True)
        time.time() - ts
        
        ts = time.time()
        group = df.groupby(['product_id']).agg({'price': ['mean']})
        group.columns = ['item_avg_price']
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['product_id'], how='left')
        matrix['item_avg_price'] = matrix['item_avg_price'].astype(np.float16)

        group = df.groupby(['date_block_num','product_id']).agg({'price': ['mean']})
        group.columns = ['date_product_avg_price']
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['date_block_num','product_id'], how='left')
        matrix['date_product_avg_price'] = matrix['date_product_avg_price'].astype(np.float16)

        lags = [1,2,3,4,5,6,12]
        matrix = lag_feature(matrix, lags, 'date_product_avg_price')

        for i in lags:
            matrix['delta_price_lag_'+str(i)] = \
                (matrix['date_product_avg_price_lag_'+str(i)] - matrix['item_avg_price']) / matrix['item_avg_price']

        def select_trend(row):
            for i in lags:
                if row['delta_price_lag_'+str(i)]:
                    return row['delta_price_lag_'+str(i)]
            return 0
            
        matrix['delta_price_lag'] = matrix.apply(select_trend, axis=1)
        matrix['delta_price_lag'] = matrix['delta_price_lag'].astype(np.float16)
        matrix['delta_price_lag'].fillna(0, inplace=True)

        fetures_to_drop = ['item_avg_price']
        for i in lags:
            fetures_to_drop += ['date_product_avg_price_lag_'+str(i)]
            fetures_to_drop += ['delta_price_lag_'+str(i)]

        matrix.drop(fetures_to_drop, axis=1, inplace=True)

        time.time() - ts
        

        matrix['month']=df['month']
        matrix['days']=df['days']

        ts = time.time()
        matrix = matrix[matrix.date_block_num > matrix.date_block_num.max()-24]
        time.time() - ts

        ts = time.time()
        def fill_na(df):
            for col in df.columns:
                if ('_lag_' in col) & (df[col].isnull().any()):
                    if ('product_cnt' in col):
                        df[col].fillna(0, inplace=True)         
            return df

        matrix = fill_na(matrix)
        print(matrix)
        # Train
        self.modelService.updateModel(matrix, company_id)
        
        # Insert
        matrix=matrix[matrix['date_block_num']==matrix['date_block_num'].max()]
        matrix['product_id']=matrix['product_id'].astype(str)
        matrix=matrix.drop(['product_cnt_month'], axis=1)
        
        self.featureDao.insertSalesFeature(matrix, 'f_sales')
        return matrix    

    def updateShopFeatureSales(self):
        df=self.orderDao.findAllOrder()
        
        # df=pd.read_csv('train.csv').drop(columns=['Unnamed: 0'])
        
        df['product_id'] = df['product_id'].astype('int64')
        df['company_id'] = df['company_id'].astype('int64')
        
        pro=self.productDao.findAllProduct()  
        pro.rename(columns={'id': 'product_id'}, inplace=True)
        pro['product_id'] = pro['product_id'].astype('int64')
        pro['company_id'] = pro['company_id'].astype('int64')
        pro['code'] = pro['code'].astype('int64')
        
        # pro=pd.read_csv('items.csv')[['product_id','category_id']]
        # pro['code']=pro['product_id']
        # pro['code'] = pro['code'].astype('int64')
        # pro['product_id'] = pro['product_id'].astype('int64')
        
        df=df.merge(pro[['product_id','code']],on=['product_id'],how='left')
        
        
        

        df['date'] = pd.to_datetime(df['complete_time'])
        df['date_block_num'] = (df['date'].dt.year - df['date'].dt.year.min()) * 12 + (df['date'].dt.month - df['date'].dt.month.min())
        
        # df['date'] = pd.to_datetime(df['complete_time'], format='%d.%m.%Y')
        
        
        df['month'] = df['date'].dt.month
        df['days'] = df['date'] + pd.offsets.MonthEnd(0)
        df['days'] = df['days'].dt.day
        
        print(df)
        if df['date_block_num'].max()<4:
            return
        
        last_month = df['date_block_num'].max()
        
        last_month_sales = df[df['date_block_num'] == last_month]
        print(last_month_sales)

        missing_products = pro[~pro['product_id'].isin(last_month_sales['product_id'])]
        print(missing_products)
        missing_products['date_block_num'] = last_month
        missing_products['month'] = df[df['date_block_num'] == last_month]['month'].max()
        missing_products['days'] = df[df['date_block_num'] == last_month]['days'].max()
        missing_products['quantity'] = 0
        print(missing_products)
        df = pd.concat([df, missing_products], ignore_index=True)




        ts = time.time()
        matrix = []
        cols = ['date_block_num','company_id','product_id']
        for i in range(df['date_block_num'].min(),df['date_block_num'].max()+1):
            sales = df[df.date_block_num==i]
            matrix.append(np.array(list(product([i], sales.company_id.unique(),sales.product_id.unique()))))
            
        matrix = pd.DataFrame(np.vstack(matrix), columns=cols)
        matrix['date_block_num'] = matrix['date_block_num']
        matrix['product_id'] = matrix['product_id']
        matrix['company_id'] = matrix['company_id']
        matrix.sort_values(cols,inplace=True)
        time.time() - ts
        print(matrix)
        groupby = df.groupby(['product_id','date_block_num','company_id']).agg({'quantity':'sum'})
        groupby.columns = ['product_cnt_month']
        groupby.reset_index(inplace=True)
        groupby.columns = ['product_id', 'date_block_num','company_id','product_cnt_month']

        matrix = matrix.merge(groupby, on = ['product_id','date_block_num','company_id'], how = 'left')
        matrix['product_cnt_month'] = matrix['product_cnt_month'].fillna(0).clip(0,20).astype(np.float16)
        print(matrix)

        
        ts = time.time()

        group = df.groupby(['date_block_num','company_id','product_id']).agg({'price': ['mean']})
        group.columns = ['date_product_avg_price']
        group.reset_index(inplace=True)

        matrix = pd.merge(matrix, group, on=['date_block_num','company_id','product_id'], how='left')
        matrix['date_product_avg_price'] = matrix['date_product_avg_price'].fillna(0).astype(np.float16)


        time.time() - ts
        print(matrix)


        # matrix['month']=df['month']
        # matrix['days']=df['days']
        
        date_feature=df[['date_block_num','month','days']].drop_duplicates()
        print(date_feature)
        matrix=matrix.merge(date_feature, on='date_block_num',how='left')

        print(matrix)
        ts = time.time()
        matrix = matrix[matrix.date_block_num > matrix.date_block_num.max()-24]
        time.time() - ts
        print(matrix)
        
        shop_features=self.featureDao.findAllCompanyFeatures()
        shop_features['company_id']=shop_features['company_id'].astype('int64')
        shop_features['type_code'] = LabelEncoder().fit_transform(shop_features['type'])
        shop_features['type']=shop_features['type_code']
        shop_features=shop_features.drop(columns=['type_code','name','location'])
        
        matrix = matrix[matrix['company_id'].isin(shop_features['company_id'])]
        matrix = matrix.merge(shop_features, on='company_id', how='left')
        matrix = matrix.merge(pro[['product_id', 'code']], on='product_id', how='left')


        new_columns = ['code'] + [col for col in matrix.columns if col != 'code']
        matrix = matrix[new_columns]
        matrix.fillna(0)
        print(matrix)

        # Train
        matrix=matrix.drop(['company_id'],axis=1)
        self.modelService.updateModel(matrix, 'shop')
        
        # Insert
        matrix=matrix[matrix['date_block_num']==matrix['date_block_num'].max()]
        matrix['product_id']=matrix['product_id'].astype(str)
        matrix['code']=matrix['code'].astype(str)
        matrix=matrix.drop(['product_cnt_month'],axis=1)

        self.featureDao.insertSalesFeature(matrix.head(50), 'f_sales_com')

        return