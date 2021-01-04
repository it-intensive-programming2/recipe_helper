import json, time
import pandas as pd

from flask import escape, Blueprint, request, session, current_app as app
from sqlalchemy import text

from main.extensions import *
from main.model import *

search_api = Blueprint('search', __name__, url_prefix='/search')

@search_api.route('/schoolList', methods=['GET'])
def get_schoolList():
    # this is server-side query.
    # trim 고,등,학,교 off
    search_text = escape(request.args.get('schoolName')).strip('고등학교')
    if not search_text or search_text == "":
        return response_with_code("<fail>:2:invalid search text")
    schoolList = SchoolInfo.query.filter(SchoolInfo.schoolName.like('%'+search_text+'%'))
    df = pd.read_sql(schoolList.statement, schoolList.session.bind)
    return response_with_code("<success>", json.loads(df.to_json(orient='records', force_ascii=False)))

@search_api.route('/univList', methods=['GET'])
def get_univList():
    search_text = escape(request.args.get('univName')).strip('대학교')
    if not search_text or search_text == "":
        return response_with_code("<fail>:2:invalid search text")
    schoolList = UnivInfo.query.filter(UnivInfo.univName.like('%'+search_text+'%'))
    df = pd.read_sql(schoolList.statement, schoolList.session.bind)
    return response_with_code("<success>", json.loads(df.to_json(orient='records', force_ascii=False)))
