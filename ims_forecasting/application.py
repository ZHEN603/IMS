from flask import Flask
from controllers.forecastingController import prediction_blueprint
from scheduler import start_scheduler

app = Flask(__name__)
app.register_blueprint(prediction_blueprint)

if __name__ == '__main__':
    start_scheduler()
    app.run(host='0.0.0.0', debug=True, port=9006)
    
