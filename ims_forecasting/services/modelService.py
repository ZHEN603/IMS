import optuna
from sklearn.metrics import mean_squared_error, r2_score
import lightgbm as lgb
from dao.modelDao import modelDao


class modelService:
    def __init__(self):
        self.path = 'Model/'
        self.pathCustomised= 'Model/Customised/'
        self.fileName = 'best_lgb_model'
        self.modelDao = modelDao()

    def updateModel(self, data, company_id):     
        if company_id == 'shop':
            data=data.drop(['product_id'],axis=1)
        
        train_block=data['date_block_num'].max()-1
        X_train = data[data.date_block_num < train_block].drop(['product_cnt_month'], axis=1)
        Y_train = data[data.date_block_num < train_block]['product_cnt_month']
        X_valid = data[data.date_block_num == train_block].drop(['product_cnt_month'], axis=1)
        Y_valid = data[data.date_block_num == train_block]['product_cnt_month']
        # Prepare the data
        train_data = lgb.Dataset(data=X_train, label=Y_train)
        valid_data = lgb.Dataset(data=X_valid, label=Y_valid)

        # Define the objective function
        def objective(trial):
            param = {
                'objective': 'regression',
                'metric': 'rmse',
                'verbosity': -1,
                'boosting_type': 'gbdt',
                'num_leaves': trial.suggest_int('num_leaves', 20, 300),
                'learning_rate': trial.suggest_float('learning_rate', 0.005, 0.2),
                'feature_fraction': trial.suggest_float('feature_fraction', 0.1, 1.0),
                'bagging_fraction': trial.suggest_float('bagging_fraction', 0.1, 1.0),
                'bagging_freq': trial.suggest_int('bagging_freq', 1, 7),
                'min_child_samples': trial.suggest_int('min_child_samples', 5, 100),
                'n_estimators': trial.suggest_int('n_estimators', 1000, 10000),
                'min_data_in_leaf': trial.suggest_int('min_data_in_leaf', 20, 100),
                'feature_pre_filter': False  # Set feature_pre_filter to false
            }

            # Set callback functions, including early stopping
            callbacks = [
                lgb.early_stopping(stopping_rounds=50)
            ]
            
            # Train the model
            gbm = lgb.train(param, train_data, valid_sets=[valid_data], callbacks=callbacks)

            # Output RMSE on the validation set
            rmse = gbm.best_score['valid_0']['rmse']
            return rmse

        # Create the study object, execute optimization
        study = optuna.create_study(direction='minimize')
        study.optimize(objective, n_trials=50)

        # Print out the best parameters
        print('Number of finished trials:', len(study.trials))
        print('Best trial:', study.best_trial.params)

        # Train the model with the best parameters
        best_params = study.best_trial.params
        best_model = lgb.train(best_params, train_data, valid_sets=[valid_data], callbacks=[lgb.early_stopping(stopping_rounds=50, verbose=True)])

        # Save the model
        if company_id == 'shop':
            file_path=self.path+self.fileName+'.txt'
            self.modelDao.modelFile('up')
        else:
            file_path = self.pathCustomised+self.fileName+'_'+company_id+'.txt'
            self.modelDao.modelFile('up',company_id)
        
        best_model.save_model(file_path)
        
        return