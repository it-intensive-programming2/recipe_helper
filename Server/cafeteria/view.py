import json, time
from datetime import datetime
from flask import jsonify, make_response, escape, Blueprint, request, session, current_app as app
from sqlalchemy import text, desc
from main.extensions import *
from main.model import *

cafeteria_api = Blueprint('cafeteria', __name__, url_prefix='/cafeteria')

@cafeteria_api.route('/read', methods=['GET'])
@login_required
def get_read_cafeMenu():
    version = request.args.get('version')
    school_id = session['school_id']
    cafe = CafeteriaInfo.query.filter_by(schoolID=school_id, version=version).first()
    if cafe:
        if str(cafe.version).split(' ')[0] == version:
            return response_with_code('<fail>:2:no need to re-download it', {'curMonth':[], 'nextMonth':[], 'version':version})
    result = CafeteriaInfo.query.filter_by(schoolID=school_id).first()
    dict_row = convert_to_dict(result)
    dict_row.pop('schoolID')
    dict_row.pop('regionID')
    dict_row['curMonth'] = json.loads(dict_row.pop('curCafeMenu'))
    dict_row['nextMonth'] = json.loads(dict_row.pop('nextCafeMenu'))
    dict_row['version'] = str(dict_row['version']).split(' ')[0]
    # print(dict_row['nextMonth'])
    return response_with_code('<success>', dict_row)
