from apscheduler.schedulers.background import BackgroundScheduler
from services.dataService import dataService

def start_scheduler():
    scheduler = BackgroundScheduler()
    update_data_service = dataService()

    scheduler.add_job(update_data_service.shopFeatureRoutine, 'interval', days=1)
    scheduler.add_job(update_data_service.salesRoutine, 'interval', days=1)
    
    scheduler.start()
