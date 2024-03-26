from dao.featureDao import featureDao
from dao.modelDao import modelDao
import lightgbm as lgb
import os
import numpy as np

class forecastingService:
    def __init__(self):
        self.featureDao = featureDao()
        self.modelDao = modelDao()
        self.path = 'Model/'
        self.pathCustomised= 'Model/Customised/'
        self.fileName = 'best_lgb_model'
    
    
    
    def getForecast(self, ids, company_id):
        res={}
        file_path = self.pathCustomised+self.fileName+'_'+company_id+'.txt'
        print(file_path)
        if os.path.exists(file_path) == False:
            if self.modelDao.modelFile('down',company_id) == False:
                return self.getSalesComForecast(ids)
            else:
                salesModel = lgb.Booster(model_file=file_path)
        else:
            salesModel = lgb.Booster(model_file=file_path)
        
        for product_id in ids:
            sale=self.featureDao.findSalesFeatureById(product_id, 'f_sales')
            if len(sale)!=0:
                sale['product_id']=sale['product_id'].astype('int64')
                res[product_id]=np.ceil(salesModel.predict(sale).clip(0, 20))[0]
            else:
                res[product_id]='No Result Yet'   
        return res
    
    
    def getSalesComForecast(self, ids):
        res={}
        file_path=self.path+self.fileName+'.txt'
        if os.path.exists(file_path) == False:
            if self.modelDao.modelFile('down') == False:
                for product_id in ids:
                    res[product_id]='No Result Yet'
                return res
            else:
                salesModel = lgb.Booster(model_file=file_path)
        else:
            salesModel = lgb.Booster(model_file=file_path)        
                
        for product_id in ids:
            saleCom=self.featureDao.findSalesFeatureById(product_id, 'f_sales_com')
            saleCom=saleCom.drop(['product_id'], axis=1)
            saleCom['code']=saleCom['code'].astype('int64')
            if len(saleCom)!=0:
                res[product_id]=np.ceil(salesModel.predict(saleCom).clip(0, 20))[0]
            else:
                res[product_id]='No Result Yet'
        return res
                        