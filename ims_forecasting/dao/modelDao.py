from google.cloud import storage
import os

class modelDao:
    def __init__(self):
        self.client = storage.Client()
        self.bucket = self.client.bucket('ims-storage')
        self.path = 'Model/'
        self.pathCustomised= 'Model/Customised/'
        self.fileName = 'best_lgb_model'
        

    def modelFile(self, type, companyId=False):
        if companyId == False:
            self.directoryExists(self.path)
            file_path=self.path+self.fileName+'.txt'
        else:
            self.directoryExists(self.pathCustomised)
            file_path=self.pathCustomised+self.fileName+'_'+companyId+'.txt'

        blob = self.bucket.blob(file_path)
        try:
            if type == 'down':
                blob.download_to_filename(file_path)
            else:
                blob.upload_from_filename(file_path)
            return True
        except Exception as e:
            return False

             
    def directoryExists(self, directory):
        if not os.path.exists(directory):
            os.makedirs(directory)