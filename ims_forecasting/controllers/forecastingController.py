from flask import Blueprint, request, jsonify
from services.forecastingService import forecastingService
prediction_blueprint = Blueprint('prediction', __name__)
forecastingService = forecastingService()

@prediction_blueprint.route('/forecast', methods=['POST'])
def forecast():
    try:
        ids = request.get_json().get('productIds')
        company_id = request.get_json().get('companyId')
        res = forecastingService.getForecast(ids,company_id)
    except Exception as e:
        print(e)
        return jsonify({
            "success": "false",
            "code": 99999,
            "meassage": "Sorry, we cannot do this now."
        })
        
    return jsonify({
        "success": "true",
        "code": 10000,
        "data": res
    })
