import json, time
from pprint import pprint as pp
from datetime import datetime
from flask import jsonify, make_response, escape, Blueprint, request, session, current_app as app
from sqlalchemy import text, desc
from main.extensions import *
from main.model import *

article_api = Blueprint('article', __name__, url_prefix='/article')

com_type = [ArticleAll, ArticleRegion, ArticleSchool]
com_type_name = ["ArticleAll", "ArticleRegion", "ArticleSchool"]
heart_type = [LikeToAll, LikeToRegion, LikeToSchool]
allowed_ids = ['allowed_all_ids', 'allowed_region_ids', 'allowed_school_ids']

time_format = "%04d/%02d/%02d %02d:%02d:%02d"

@article_api.route('/report', methods=['GET'])
@login_required
@allowed_access
@user_have_write_right
def get_report_article():
    communityType = int(request.args.get('communityType'))
    articleID = int(request.args.get('articleID'))
    communityID = int(request.args.get('communityID'))
    community_type_name = com_type_name[communityType]
    article = com_type[communityType]
    # query db and change to dict
    query_result = article.query.filter_by(articleID=articleID, communityID=communityID).first()
    report_query = ArticleReport.query.filter_by(articleID=articleID, communityID=communityID).first()
    if not query_result:
        # delete report item if article has been already deleted.
        if not report_query:
            db.session.delete(query_result)
            db.session.commit()
        return response_with_code("<fail>:3:no article")
    dict_value = convert_to_dict(query_result)
    # if article does not reported before
    if not report_query:
        reportUser = [session['user_id']]
        report = ArticleReport(articleID = dict_value['articleID'], communityID = dict_value['communityID'], articleType = community_type_name,
        userID = dict_value['userID'], title = dict_value['title'], content = dict_value['content'], reportNum = 1, reportUser = json.dumps(reportUser))
        db.session.add(report)
        db.session.commit()
    # if article reported before
    else:
        # check if user has already reported this article before
        report_user_ids = json.loads(report_query.reportUser)
        if session['user_id'] in report_user_ids:
            return response_with_code("<fail>:2:Already Reported")
        # add report user id
        report_user_ids.append(session['user_id'])
        report_query.reportNum += 1
        report_query.reportUser = json.dumps(report_user_ids)
        # if reported by more than 5 person, it will be deleted.
        if report_query.reportNum >= 5:
            user = UserInfo.query.filter_by(userID = report_query.userID).first()
            db.session.delete(report_query)
            db.session.delete(query_result)
            if not user:
                db.session.commit()
                return response_with_code("<success>")

            if user.banned:
                user.banned += 1
            else:
                user.banned = 1
            # if user is reported by more than 3 times
            if user.banned > 2:
                signout_user = SignOutUser(userID=user.userID, writtenTime=get_cur_date())
                db.session.add(signout_user)
                db.session.delete(user)
        db.session.commit()
    #  increase view number
    return response_with_code("<success>")

@article_api.route('/read', methods=['GET'])
@login_required
@allowed_access
def get_read_article():
    communityType = int(request.args.get('communityType'))
    articleID = int(request.args.get('articleID'))
    communityID = int(request.args.get('communityID'))
    article = com_type[communityType]
    heart = heart_type[communityType]
    heart_pushed = 0
    reported = 0
    # query db and change to dict
    query_result = article.query.filter_by(articleID=articleID, communityID=communityID).first()
    if not query_result:
        return response_with_code("<fail>:3:no article")
    query_result2 = heart.query.filter_by(articleID=articleID, userID=session['user_id']).first()
    if query_result2:
        heart_pushed = 1
    query_result3 = ArticleReport.query.filter_by(articleID=articleID, communityID=communityID).first()
    if query_result3:
        if session['user_id'] in json.loads(query_result3.reportUser):
            reported = 1
    target_article = convert_to_dict(query_result)
    writer = target_article.pop('userID')
    target_article['edit'] = 1 if writer == session['user_id'] else 0
    target_article['heartPushed'] = heart_pushed
    target_article['reported'] = reported
    #  increase view number
    query_result.viewNumber += 1
    db.session.commit()
    return response_with_code("<success>", target_article)

@article_api.route('/modifyHeart', methods=['GET'])
@login_required
@allowed_access
def get_modify_heart():
    communityType = int(request.args.get('communityType'))
    articleID = int(request.args.get('articleID'))
    communityID = int(request.args.get('communityID'))
    op = int(request.args.get('op'))
    article = com_type[communityType]
    heart = heart_type[communityType]
    # query db and change to dict
    query_result = article.query.filter_by(articleID=articleID, communityID=communityID).first()
    if not query_result:
        return response_with_code("<fail>:2:no article")
    query_result2 = heart.query.filter_by(articleID=articleID, userID=session['user_id']).first()
    if op == 1:
        if query_result2:
            return response_with_code("<fail>:2:already add heart")
        else:
            new_heart = heart(articleID=articleID, userID=session['user_id'])
            db.session.add(new_heart)
    elif op == 0:
        if not query_result2:
            return response_with_code("<fail>:2:did not add heart")
        else:
            db.session.delete(query_result2)
    #  increase view number
    if op == 1:
        query_result.heart += 1
    else:
        query_result.heart -= 1
    db.session.commit()
    return response_with_code("<success>")

# For future use
# request.on_json_loading_failed = on_json_loading_failed_return_dict
# def on_json_loading_failed_return_dict(e):
#     return {}

@article_api.route('/write', methods=['POST'])
@login_required
@allowed_access
@user_have_write_right
def post_write_article():
    written_info = request.json
    if written_info is None:
        return response_with_code("<fail>:2:no post data")
    # get time and nickName info
    now = time.localtime()
    written_time = time_format % (now.tm_year, now.tm_mon, now.tm_mday, now.tm_hour, now.tm_min, now.tm_sec)
    nickname = '익명' if written_info['isAnonymous'] else session['nick_name']
    # generate articleID
    article_id = (written_info['communityID'] % 100) * 10000000 + get_random_numeric_value(2) * 100000 + current_milli_time()
    # create article instante
    article = com_type[written_info['communityType']]
    new_article = article(communityID=written_info['communityID'],
    userID=session['user_id'],
    nickName=nickname,
    title=written_info['title'],
    content=written_info['content'],
    writtenTime=written_time, heart = 0, viewNumber = 0, reply = 0)
    # add school and region id
    if written_info['communityType'] == 1:
        new_article.regionID = session['region_id']
    elif written_info['communityType'] == 2:
        new_article.schoolID = session['school_id']
    db.session.add(new_article)
    db.session.commit()
    return response_with_code("<success>")


@article_api.route('/delete', methods=['GET'])
@login_required
@allowed_access
@user_have_write_right
def get_delete_article():
    communityType = int(request.args.get('communityType'))
    articleID = int(request.args.get('articleID'))
    communityID = int(request.args.get('communityID'))
    article = com_type[communityType]
    # query db and check if user wrote it
    query_result = article.query.filter_by(articleID=articleID, communityID=communityID).first()
    if not query_result:
        return response_with_code("<fail>:2:no article")
    if query_result.userID != session['user_id']:
        return response_with_code("<fail>:2:no right to delete")
    #  delete target article
    db.session.delete(query_result)
    db.session.commit()
    return response_with_code("<success>")


@article_api.route('/articleList', methods=['GET'])
@login_required
@allowed_access
def get_article_list():
    communityType = int(request.args.get('communityType'))
    communityID = int(request.args.get('communityID'))
    writtenAfter = request.args.get('writtenAfter')
    article = com_type[communityType]

    if writtenAfter == 'latest':
        rows = article.query.filter_by(communityID=communityID).order_by(desc(article.writtenTime)).limit(15).all()
    else:
        rows = article.query.filter(article.writtenTime<writtenAfter, article.communityID==communityID).\
        order_by(desc(article.writtenTime)).limit(15).all()
    articles = []
    for row in rows:
        dict_row = convert_to_dict(row)
        dict_row.pop('userID')
        articles.append(dict_row)
    return response_with_code("<success>", articles)

@article_api.route('/hotArticleList', methods=['GET'])
@login_required
def get_hot_article_list():
    articles = []
    for id in range(3):
        community = com_type[id]
        for communityID in session[allowed_ids[id]]:
            # load heart and calculate max heart value
            # print('communityID',communityID)
            hearts = db.session.query(community.heart).filter_by(communityID=communityID).all()
            if len(hearts) == 0:
                continue
            max_hearts = max([heart[0] for heart in hearts])
            hot_article = community.query.filter_by(heart=max_hearts, communityID=communityID).first()
            if hot_article:
                hot_article = convert_to_dict(hot_article)
                hot_article.pop('userID')
                hot_article['communityType'] = id
                articles.append(hot_article)
    # pp(articles)
    return response_with_code("<success>", articles)

@article_api.route('/latestArticleList', methods=['GET'])
@login_required
def get_latest_article_list():
    articles = []
    for id in range(3):
        community = com_type[id]
        for communityID in session[allowed_ids[id]]:
            article = community.query.order_by(desc(community.writtenTime)).filter_by(communityID=communityID).first()
            if article:
                article = convert_to_dict(article)
                article.pop('userID')
                article['communityType'] = id
                articles.append(article)

    return response_with_code("<success>", articles)
