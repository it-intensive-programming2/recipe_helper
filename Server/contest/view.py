import json, time
from datetime import datetime
from flask import jsonify, make_response, escape, Blueprint, request, session, current_app as app
from sqlalchemy import text, desc
from main.extensions import *
from main.model import *

contest_api = Blueprint('contest', __name__, url_prefix='/contest')

@contest_api.route('/getList', methods=['GET'])
@login_required
def get_read_contest():
    version = int(request.args.get('version'))
    contests = [convert_to_dict(row) for row in  ContestInfo.query.filter(ContestInfo.contestID>version).all()]
    return response_with_code('<success>', contests)
