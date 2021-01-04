import json, time
from datetime import datetime
from flask import jsonify, make_response, escape, Blueprint, request, session, current_app as app
from sqlalchemy import text, desc
from main.extensions import *
from main.model import *

reply_api = Blueprint('reply', __name__, url_prefix='/reply')

com_type = [ArticleAll, ArticleRegion, ArticleSchool]
reply_type = [ReplyAll, ReplyRegion, ReplySchool]
reReply_type = [ReReplyAll, ReReplyRegion, ReReplySchool]
allowed_ids = ['allowed_all_ids', 'allowed_region_ids', 'allowed_school_ids']

time_format = "%04d/%02d/%02d %02d:%02d:%02d"

@reply_api.route('/read', methods=['GET'])
@login_required
@allowed_access
def get_read_reply():
    communityType = int(request.args.get('communityType'))
    communityID = int(request.args.get('communityID'))
    articleID = int(request.args.get('articleID'))
    reply = reply_type[communityType]
    re_reply = reReply_type[communityType]
    # query db and change to dict
    query_result = reply.query.filter_by(articleID=articleID, communityID=communityID).order_by(reply.writtenTime).all()
    if not query_result:
        return response_with_code('<success>', [])
    # process replys
    replys = []
    for row in query_result:
        re = convert_to_dict(row)
        re['parentReplyID'] = 0
        re['edit'] = True if re.pop('userID') == session['user_id'] else False
        replys.append(re)
    # process re-replys
    re_replys = []
    query_result = re_reply.query.filter_by(articleID=articleID, communityID=communityID).order_by(re_reply.writtenTime).all()
    if query_result:
        for row in query_result:
            re = convert_to_dict(row)
            re['edit'] = True if re.pop('userID') == session['user_id'] else False
            re_replys.append(re)
    return response_with_code("<success>", {"replys":replys, "reReplys":re_replys})

# For future use
# request.on_json_loading_failed = on_json_loading_failed_return_dict
# def on_json_loading_failed_return_dict(e):
#     return {}

@reply_api.route('/write', methods=['POST'])
@login_required
@allowed_access
@user_have_write_right
def post_write_reply():
    written_info = request.json
    if written_info is None:
        return response_with_code('<fail>:2:no data')
    # increase reply number for article
    article = com_type[written_info['communityType']]
    target_article = article.query.filter_by(articleID=written_info['articleID'], communityID=written_info['communityID']).first()
    if target_article == None:
        return response_with_code('<fail>:2:no article ')
    writter_id = target_article.userID
    target_article.reply += 1
    # get time and nickName info
    now = time.localtime()
    written_time = time_format % (now.tm_year, now.tm_mon, now.tm_mday, now.tm_hour, now.tm_min, now.tm_sec)
    nickname = '익명' if written_info['isAnonymous'] else session['nick_name']
    # generate articleID
    reply_id = (written_info['communityID'] % 100) * 10000000 + get_random_numeric_value(2) * 100000 + current_milli_time()
    # create article instante
    reply= reply_type[written_info['communityType']] if written_info['parentID'] == 0 \
        else reReply_type[written_info['communityType']]
    new_reply = reply(articleID = written_info['articleID'],
    communityID=written_info['communityID'],
    userID=session['user_id'],
    nickName=nickname,
    content=written_info['content'],
    writtenTime=written_time)
    # add school and region id
    if written_info['communityType'] == 1:
        new_reply.regionID = session['region_id']
    elif written_info['communityType'] == 2:
        new_reply.schoolID = session['school_id']
    # add parent id if rereply are written
    if written_info['parentID'] != 0:
        new_reply.parentReplyID = written_info['parentID']
    db.session.add(new_reply)
    db.session.commit()
    # send push alarm
    fcm_token = UserInfo.query.filter_by(userID = writter_id).first().fcmToken
    if fcm_token:
        title = "새로운 댓글이 달렸습니다."
        body = {"communityType":written_info['communityType'], "articleID":written_info['articleID'], "communityID":written_info['communityID'],
        "content":written_info['content'], "time":get_cur_date(), "type":0}
        send_push_alarm(fcm_token, title, json.dumps(body))
    return response_with_code('<success>')

@reply_api.route('/delete', methods=['GET'])
@login_required
@allowed_access
@user_have_write_right
def get_delete_reply():
    written_info = {key:int(val[0]) for key, val in dict(request.args).items()}
    print(written_info)
    reply= reply_type[written_info['communityType']] if written_info['isRereply'] == 0 \
        else reReply_type[written_info['communityType']]
    # query db and change to dict
    query_result = reply.query.filter_by(replyID=written_info['replyID'], communityID=written_info['communityID']).first()
    if not query_result:
        return response_with_code('<fail>:2:no rely')
    # check authority replys
    if query_result.userID != session['user_id']:
        return response_with_code('<fail>:2:no right to delete')
    # decrease reply number for article
    article = com_type[written_info['communityType']]
    target_article = article.query.filter_by(articleID=written_info['articleID']).first()
    target_article.reply -= 1
    db.session.delete(query_result)
    db.session.commit()
    return response_with_code("<success>")
