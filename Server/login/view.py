import json, time, requests, re
import random
import string
import hashlib
from pprint import pprint as pp

import pandas as pd
from usernames import is_safe_username
from flask import jsonify, make_response, escape, Blueprint, request, session, current_app as app
from sqlalchemy import text
from Crypto.PublicKey import RSA


from main.extensions import *
from main.model import *

login_api = Blueprint('auth', __name__, url_prefix='/auth')

@login_api.route('/logout', methods=['GET'])
@login_required
def get_logout():
    session.clear()
    return response_with_code('<success>')

@login_api.route('/withdraw', methods=['GET'])
@login_required
def get_signout():
    uid = request.args.get('id')
    if session['user_id'] != uid:
        response_with_code('<Fail>:2:args needment fail')
    user = UserInfo.query.filter_by(userID = uid).first()
    signout_user = SignOutUser(userID=uid, writtenTime=get_cur_date())
    session.clear()
    db.session.add(signout_user)
    db.session.delete(user)
    db.session.commit()
    return response_with_code('<success>')

@login_api.route('/login', methods=['GET'])
def get_login():
    uid = request.args.get('id')
    user_access_token = request.args.get('token')
    #check if args exist
    if not uid or not user_access_token:
        return response_with_code('<Fail>:2:args needment fail')
    # check sender access token is valid
    token_info = get_request(user_access_token, '/v1/user/access_token_info')
    if not token_info or int(uid) != int(token_info['id']):
        return response_with_code('<Fail>:2:token expired')
    # check reasign in 30 days
    sign_user = SignOutUser.query.filter_by(userID = uid).first()
    if sign_user:
        return response_with_code('<Fail>:2:You can not re assign in 30 days ')
    # check user is signed up
    result = UserInfo.query.filter_by(userID = int(uid)).first()
    if not result:
        return response_with_code('<Fail>:3:user_id need to sign up')
    # make json response body
    dict_resp = convert_to_dict(result)
    dict_resp['status'] = 'success'
    # query school info
    schoolID = dict_resp.pop('schoolID')
    school_dict = convert_to_dict(SchoolInfo.query.filter_by(schoolID = schoolID).first())
    school_dict['schoolGender'] = school_dict.pop('gender')
    dict_resp.update(school_dict)
    dict_resp.pop('regionID')
    dict_resp.pop('schoolID')
    dict_resp.pop('userID')
    dict_resp['userName'] = dict_resp.pop('studentName')
    dict_resp['signupDate'] = str(dict_resp.pop('signupDate'))
    # community id allowed for each user
    coms_all = [convert_to_dict(row) for row in CommunityAll.query.all()]
    community_all_ids = [com['communityID'] for com in coms_all]
    community_all_ids = list(filter(lambda x : x < 100, community_all_ids))
    # convert query string to dict
    coms_school = []
    for row in CommunitySchool.query.filter_by(schoolID=result.schoolID).all():
        dict_row = convert_to_dict(row)
        dict_row.pop('schoolID')
        coms_school.append(dict_row)
    community_school_ids = [com['communityID'] for com in coms_school]
    # convert query string to dict
    coms_region = []
    for row in CommunityRegion.query.filter_by(regionID=result.regionID).all():
        dict_row = convert_to_dict(row)
        dict_row.pop('regionID')
        coms_region.append(dict_row)
    community_region_ids = [com['communityID'] for com in coms_region]
    # pass it to user response
    dict_resp['comAll'] = coms_all
    dict_resp['comRegion'] = coms_region
    dict_resp['comSchool'] = coms_school
    dict_resp['authorized'] = result.authorized
    # make session
    session['user_id'] = result.userID
    session['school_id'] = result.schoolID
    session['region_id'] = result.regionID
    session['fcm_token'] = result.fcmToken
    session['allowed_all_ids'] = community_all_ids
    session['allowed_school_ids'] = community_school_ids
    session['allowed_region_ids'] = community_region_ids
    session['allowed_ids'] = community_all_ids + community_school_ids + community_region_ids
    session['authorized'] = result.authorized
    session['nick_name'] = result.nickName
    session['grade'] = result.grade
    return response_with_code("<success>", dict_resp)

@login_api.route('/kakaoOauth', methods=['GET'])
def post_oauth():
    print(request)

@login_api.route('/registerFCM', methods=['GET'])
@login_required
def get_registerFCM():
    fcm_token = request.args.get('token')
    user = UserInfo.query.filter_by(userID = session['user_id']).first()
    user.fcmToken = fcm_token
    session['fcm_token'] = fcm_token
    db.session.commit()
    print(fcm_token)
    return response_with_code("<success>")

@login_api.route('/kakaoSignup', methods=['POST'])
def post_signup():
    pattern = re.compile("^(?!_$)(?![-.])(?!.*[_.-]{2})[가-힣a-zA-Z0-9_.-]+(?<![.-])$")
    user_info = request.json
    user_access_token = escape(user_info['accessToken'])
    user_id = escape(user_info['userID'])
    email, gender, ageRange = escape(user_info['email']), escape(user_info['gender']), escape(user_info['ageRange'])
    nickName, grade = escape(user_info['nickName']), escape(user_info['grade'])
    user_name = str(escape(user_info['userName']))
    # check sender access token is valid
    token_info = get_request(user_access_token, '/v1/user/access_token_info')
    if not token_info or str(user_id) != str(token_info['id']):
        return response_with_code('<Fail>:2:token expired')
    # check sender info is valid
    auth_user_info = get_request(user_access_token, '/v2/user/me')
    # if not auth_user_info or auth_user_info['kakao_account']['email'] != email or \
    # auth_user_info['kakao_account']['gender'] != gender or \
    # auth_user_info['kakao_account']['age_range'] != ageRange:
    if not auth_user_info or auth_user_info['kakao_account']['email'] != email:
        return response_with_code('<Fail>:2:sender info is not valid')
    # check reasign in 30 days
    sign_user = SignOutUser.query.filter_by(userID = user_id).first()
    if sign_user:
        return response_with_code('<Fail>:2:You can not re assign in 30 days ')
    # check if nickName and grade is valid
    ## Todo : nickName validation test
    if not pattern.search(nickName).group() or int(grade) < 9 or int(grade) > 13:
        return response_with_code('<Fail>:2:nickname or grade is not valid')
    # check if user is possible to enroll school
    schoolInfo = SchoolInfo.query.filter_by(schoolID = user_info['schoolID']).first()
    if not schoolInfo:
        return response_with_code("<Fail>:2:school id is not good")
    sch_name = schoolInfo.schoolName
    reg_name = schoolInfo.regionName
    sch_gen = schoolInfo.gender
    sch_reg = schoolInfo.regionID
    if (sch_gen == 1 and gender != 'male') or (sch_gen == 2 and gender != 'female'):
        return response_with_code("<Fail>:2:user gender is not same as school")
    if str(ageRange) not in ['20~29', '14~19'] or str(ageRange) == '20~29' and int(grade) < 13:
        print(str(ageRange))
        return response_with_code("<Fail>:2:user age don't go school any more")
    # check if user has already registered
    if UserInfo.query.filter_by(userID = user_id).first():
        return response_with_code("<Fail>:2:alread registered")
    # store to user info to db
    gender = 1 if str(gender) == 'male' else 2
    age = 1 if str(ageRange) == "14~19" else 2
    user = UserInfo(userID=int(user_id), schoolID=user_info['schoolID'], regionID=sch_reg,
    email=str(email), grade=int(grade), age=age, gender=gender, nickName=str(nickName),
    studentName=user_name,schoolName=sch_name,regionName=reg_name,authorized=0,signupDate=get_cur_date(),fcmToken="")
    db.session.add(user)
    db.session.commit()
    return response_with_code('<success>')

def get_request(token, url):
    headers = {
    'Authorization': 'Bearer ' + token,
    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
    }
    BASE_URL = 'https://kapi.kakao.com'
    URL = BASE_URL + url
    res = requests.get(URL, headers=headers)
    res_json = res.json()
    return res_json
