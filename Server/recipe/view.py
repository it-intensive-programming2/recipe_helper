import json
import os
import time
import pandas as pd
from pprint import pprint as pp
from tqdm import tqdm
import cv2
import numpy as np
import re
from glob import glob

from flask import escape, Blueprint, request, session, current_app as app
from sqlalchemy import text, desc
from sqlalchemy.sql.expression import func, select
from werkzeug.utils import secure_filename

from recipe.tool import getCalorie, generateWordCloud
from main.extensions import *
from main.model import *

recipe_api = Blueprint("recipe", __name__, url_prefix="/recipe")

BASICPROFILE = "https://postfiles.pstatic.net/MjAyMDEyMTBfMTU2/MDAxNjA3NTQ1NzU1ODg4.sGR0T12MvQQoIeQgMhzqVY0Fks9K9ffE_knj6pJT9V4g.Wfe-s5geNXd_TuIa4y6Xs61BzaUti_5FfOyeVOvQPCwg.PNG.mangusn1/profile.png?type=w966"

BAESDIR = os.path.dirname(__file__)


@recipe_api.route("/getWordCloud", methods=["GET"])
def getWordCloud():
    userID = escape(request.args.get("userID"))
    regenerate = escape(request.args.get("regenerate"))

    if regenerate == "True":
        df = pd.read_csv("/var/www/Appserver3/recipe/cat2_eng.csv")

        word_list = []
        histories = ClickHistory.query.filter_by(userID=userID).all()

        for history in histories:
            try:
                word_list.append(df[df.id == history.recipeID].cat2.values[0].strip())
            except Exception as identifier:
                pass
                # print("ERROR ", history.recipeID)

        scraps = ClickHistory.query.filter_by(userID=userID).all()
        for scrap in scraps:
            try:
                word = df[df.id == scrap.recipeID].cat2.values[0].strip()
                word_list.append(word)
                word_list.append(word)
                word_list.append(word)
                word_list.append(word)
                word_list.append(word)
            except Exception as identifier:
                pass
                # print("ERROR ", scrap.recipeID)

        if len(word_list) == 0:
            return response_with_code("<success>", "")

        generateWordCloud(word_list, userID)

    result = ""

    try:
        with open("/var/www/Appserver3/recipe/wc_image/" + userID + ".txt", "r") as f:
            result = f.read()
    except Exception as identifier:
        pass

    return response_with_code("<success>", result[2:-1])


@recipe_api.route("/getCalories", methods=["GET"])
def getCalories():
    userGender = escape(request.args.get("userGender"))
    userAgeRange = escape(request.args.get("userAgeRange"))
    recipeID = escape(request.args.get("recipeID"))

    # 10대, 20대, 30대~40대, 50대, 60대 이상
    male_calorie = [2700, 2600, 2400, 2200, 2000]
    female_calorie = [2000, 2100, 1900, 1800, 1600]

    total_calorie = 2500
    if userGender == "male":
        if userAgeRange[0] == 2:
            total_calorie = male_calorie[1]
        elif userAgeRange[0] == 3 or userAgeRange[0] == 4:
            total_calorie = male_calorie[2]
        elif userAgeRange[0] == 5:
            total_calorie = male_calorie[3]
        elif userAgeRange[0] == 6:
            total_calorie = male_calorie[4]
    else:
        if userAgeRange[0] == 2:
            total_calorie = female_calorie[1]
        elif userAgeRange[0] == 3 or userAgeRange[0] == 4:
            total_calorie = female_calorie[2]
        elif userAgeRange[0] == 5:
            total_calorie = female_calorie[3]
        elif userAgeRange[0] == 6:
            total_calorie = female_calorie[4]

    result = getCalorie(recipeID)

    nuts = {
        "calories": result[0],
        "carbs": result[1],
        "fat": result[2],
        "protein": result[3],
        "percentage": int(re.sub("kcal", "", result[0])) / total_calorie,
    }

    return response_with_code("<success>", nuts)


@recipe_api.route("/testPullPost", methods=["GET"])
def testPullPost():

    post = PostInfo.query.first()

    post = convert_to_dict(post)

    posts = []

    images_dir = glob(BAESDIR + "/post_image/" + "18" + "/*")
    images = []

    for image_dir in images_dir:
        with open(image_dir, "r") as file:
            strings = file.readlines()
            strings = "".join(strings)
            images.append(strings)

    post["images"] = images
    posts.append(post)
    return response_with_code("<success>", posts)


@recipe_api.route("/addHistory", methods=["GET"])
def addHistory():
    userID = escape(request.args.get("userID"))
    recipeID = escape(request.args.get("recipeID"))
    recipeClass = escape(request.args.get("recipeClass"))

    history = ClickHistory(
        userID=userID,
        recipeID=recipeID,
        recipeClass=recipeClass,
    )
    db.session.add(history)
    db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/isScrap", methods=["GET"])
def isScrap():
    userID = escape(request.args.get("userID"))
    recipeID = escape(request.args.get("recipeID"))

    query = Scrap.query.filter_by(userID=userID).filter_by(recipeID=recipeID).first()

    if query is None:
        return response_with_code("<fail>:5:not scrap")
    else:
        return response_with_code("<success>")


@recipe_api.route("/setScrap", methods=["GET"])
def setScrap():
    userID = escape(request.args.get("userID"))
    recipeID = escape(request.args.get("recipeID"))
    recipeClass = escape(request.args.get("recipeClass"))

    query = Scrap.query.filter_by(userID=userID).filter_by(recipeID=recipeID).first()

    if query is None:
        scrap = Scrap(recipeID=recipeID, userID=userID, recipeClass=recipeClass)
        db.session.add(scrap)
        db.session.commit()

    else:
        Scrap.query.filter_by(userID=userID).filter_by(recipeID=recipeID).delete()

        db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/loadScrap", methods=["GET"])
def loadScrap():
    userID = escape(request.args.get("userID"))
    queries = Scrap.query.filter_by(userID=userID).all()
    recipes = []

    for query in queries:
        recipe = Recipe.query.filter_by(recipeID=query.recipeID).first()
        recipes.append(convert_to_dict(recipe))

    recipes.reverse()

    return response_with_code("<success>", recipes)


@recipe_api.route("/loadRecommendRecipe3", methods=["GET"])
def loadRecommendRecipe3():
    ingredient_string = escape(request.args.get("ingredientString"))

    ingredient_list = ingredient_string.split(" ")

    ids, delete_list = find_recipe_with_ingredient(ingredient_list)

    if len(ids) > 50:
        ids = ids[:50]

    recipes = []

    for id in ids:
        query = Recipe.query.filter_by(recipeID=id).first()
        recipe = convert_to_dict(query)
        recipes.append(recipe)

    delete_list = list(filter(None, delete_list))

    return json.dumps(
        {"status": "<success>", "body": recipes, "delete_list": ", ".join(delete_list)}
    )


@recipe_api.route("/loadRecommendRecipe2", methods=["GET"])
def loadRecommendRecipe2():

    userAge = escape(request.args.get("userAgeRange"))

    specific_user_queries = RecipeUser.query.filter_by(ageRange=userAge).limit(20).all()

    dict_class = dict()
    total_click = 0

    for specific_user in specific_user_queries:
        histories = (
            ClickHistory.query.filter_by(userID=specific_user.userID)
            .order_by(desc(ClickHistory.recipeClass))
            .limit(100)
            .all()
        )
        for history in histories:
            total_click += 1
            if history.recipeClass not in dict_class.keys():
                dict_class[history.recipeClass] = 1
            else:
                dict_class[history.recipeClass] += 1

        scraps = (
            Scrap.query.filter_by(userID=specific_user.userID)
            .order_by(desc(Scrap.recipeClass))
            .limit(100)
            .all()
        )
        for scrap in scraps:
            total_click += 10
            if scrap.recipeClass not in dict_class.keys():
                dict_class[scrap.recipeClass] = 10
            else:
                dict_class[scrap.recipeClass] += 10

    dict_class = sorted(dict_class.items(), key=lambda item: item[1], reverse=True)

    recipes = []

    for key, value in dict_class:
        lim = int(50 * value / total_click)
        queries = (
            Recipe.query.filter_by(classNum=key).order_by(func.rand()).limit(lim).all()
        )
        for query in queries:
            recipe = convert_to_dict(query)
            recipes.append(recipe)

    queries = Recipe.query.order_by(func.rand()).limit(20).all()

    for query in queries:
        recipe = convert_to_dict(query)
        recipes.append(recipe)

    return response_with_code("<success>", recipes)


@recipe_api.route("/loadRecommendRecipe1", methods=["GET"])
def loadRecommendRecipe1():

    userID = escape(request.args.get("userID"))

    dict_class = dict()
    total_click = 0

    histories = (
        ClickHistory.query.filter_by(userID=userID)
        .order_by(desc(ClickHistory.recipeClass))
        .limit(100)
    )
    for history in histories:
        total_click += 1
        if history.recipeClass not in dict_class.keys():
            dict_class[history.recipeClass] = 1
        else:
            dict_class[history.recipeClass] += 1

    scraps = (
        Scrap.query.filter_by(userID=userID)
        .order_by(desc(Scrap.recipeClass))
        .limit(100)
    )
    for scrap in scraps:
        total_click += 10
        if scrap.recipeClass not in dict_class.keys():
            dict_class[scrap.recipeClass] = 10
        else:
            dict_class[scrap.recipeClass] += 10

    dict_class = sorted(dict_class.items(), key=lambda item: item[1], reverse=True)

    recipes = []

    for key, value in dict_class:
        lim = int(50 * value / total_click)
        queries = (
            Recipe.query.filter_by(classNum=key).order_by(func.rand()).limit(lim).all()
        )
        for query in queries:
            recipe = convert_to_dict(query)
            recipes.append(recipe)

    queries = Recipe.query.order_by(func.rand()).limit(20).all()

    for query in queries:
        recipe = convert_to_dict(query)
        recipes.append(recipe)

    return response_with_code("<success>", recipes)


@recipe_api.route("/loadHotRecipe", methods=["GET"])
def loadHotRecipe():
    dict_class = dict()

    histories = (
        ClickHistory.query.order_by(desc(ClickHistory.recipeID)).limit(100).all()
    )
    for history in histories:
        if history.recipeID not in dict_class.keys():
            dict_class[history.recipeID] = 1
        else:
            dict_class[history.recipeID] += 1

    scraps = Scrap.query.order_by(desc(Scrap.recipeID)).limit(100).all()
    for scrap in scraps:
        if scrap.recipeID not in dict_class.keys():
            dict_class[scrap.recipeID] = 10
        else:
            dict_class[scrap.recipeID] += 10

    dict_class = sorted(dict_class.items(), key=lambda x: x[1], reverse=True)

    recipes = []
    idx = 0

    for key, value in dict_class:
        if idx > 7:
            break
        query = Recipe.query.filter_by(recipeID=key).first()
        recipe = convert_to_dict(query)
        recipes.append(recipe)
        idx += 1

    recipes = recipes[:8]

    return response_with_code("<success>", recipes)


@recipe_api.route("/loadPost", methods=["GET"])
def loadPost():

    queries = PostInfo.query.all()

    posts = []

    for query in queries:

        post = convert_to_dict(query)
        writerID = post.pop("writerID")
        writer = RecipeUser.query.filter_by(userID=writerID).first()
        post["writerNickname"] = writer.nickName
        if writer.profileUrl == "":
            post["writerProfileUrl"] = BASICPROFILE
        else:
            post["writerProfileUrl"] = writer.profileUrl

        images = []
        images_dir = glob(BAESDIR + "/post_image/" + str(post["postID"]) + "/*")
        for image_dir in images_dir:
            with open(image_dir, "r") as file:
                strings = file.readlines()
                strings = "".join(strings)
                images.append(strings)

        post["images"] = images

        posts.append(post)

    posts.reverse()
    return response_with_code("<success>", posts)


@recipe_api.route("/loadComment", methods=["GET"])
def loadComment():

    postID = escape(request.args.get("postID"))
    queries = PostCommentInfo.query.filter_by(postID=postID).all()

    comments = []

    for query in queries:
        comment = convert_to_dict(query)
        writerID = comment.pop("writerID")
        writer = RecipeUser.query.filter_by(userID=writerID).first()
        comment["writerNickname"] = writer.nickName

        if writer.profileUrl == "":
            writer.profileUrl = BASICPROFILE

        comment["writerProfileUrl"] = writer.profileUrl
        comments.append(comment)

    return response_with_code("<success>", comments)


@recipe_api.route("/uploadComment", methods=["GET"])
def uploadComment():

    id = escape(request.args.get("id"))
    content = escape(request.args.get("content"))
    postID = escape(request.args.get("postID"))

    comment = PostCommentInfo(
        postID=postID,
        writerID=id,
        content=content,
    )

    post = PostInfo.query.filter_by(postID=postID).first()
    post.comment += 1
    db.session.add(post)
    db.session.add(comment)
    db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/uploadPost", methods=["GET"])
def uploadPost():
    id = escape(request.args.get("id"))
    content = escape(request.args.get("content"))
    title = escape(request.args.get("title"))

    post = PostInfo(
        writerID=id,
        content=content,
        title=title,
        heart=0,
        comment=0,
        heartUser="",
    )
    db.session.add(post)
    db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/uploadPost2", methods=["POST"])
def uploadPost2():
    userID = request.form.get("userID")
    title = request.form.get("title")
    content = request.form.get("content")

    post = PostInfo(
        writerID=userID,
        content=content,
        title=title,
        heart=0,
        comment=0,
        heartUser="",
    )
    db.session.add(post)
    db.session.commit()

    newestPost = (
        PostInfo.query.filter_by(writerID=userID)
        .order_by(desc(PostInfo.postID))
        .first()
    )

    directory = BAESDIR + "/post_image/" + str(newestPost.postID)
    if not os.path.exists(directory):
        os.makedirs(directory)

    images = request.files.getlist("images")
    for idx, image in enumerate(images):
        image.save(directory + "/" + secure_filename(image.filename))

    return response_with_code("<success>")


@recipe_api.route("/changeUserInfo", methods=["GET"])
def changeUserInfo():
    userID = escape(request.args.get("userID"))
    allergy = escape(request.args.get("allergy"))
    disease = escape(request.args.get("disease"))

    user = RecipeUser.query.filter_by(userID=userID).first()

    if user is None:
        return response_with_code("<fail>:3:not registered in our server")

    if allergy != "":
        user.allergy = allergy

    if disease != "":
        user.disease = disease

    db.session.add(user)
    db.session.commit()

    user = RecipeUser.query.filter_by(userID=userID).first()

    return response_with_code("<success>", convert_to_dict(user))


@recipe_api.route("/pushHeart", methods=["GET"])
def pushHeart():
    userID = escape(request.args.get("userID"))
    postID = escape(request.args.get("postID"))

    query = PostHeart.query.filter_by(postID=postID).filter_by(userID=userID).first()

    if query is None:
        postHeart = postHeart(postID=postID, userID=userID)

        post = PostInfo.query.filter_by(postID=postID).first()
        post.heart += 1

        db.session.add(post)
        db.session.add(postHeart)
        db.session.commit()
    else:
        PostHeart.query.filter_by(postID=postID).filter_by(userID=userID).delete()

        post = PostInfo.query.filter_by(postID=postID).first()
        post.heart -= 1

        db.session.add(post)
        db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/checkUser", methods=["GET"])
def checkUser():
    id = escape(request.args.get("id"))
    query_result = RecipeUser.query.filter_by(userID=id).first()

    if query_result is None:
        return response_with_code("<fail>:3:not registered in our server")

    user = convert_to_dict(query_result)

    if user["profileUrl"] is None or user["profileUrl"] == "":
        user["profileUrl"] = BASICPROFILE

    return response_with_code("<success>", user)


@recipe_api.route("/loadTasteRecipe", methods=["GET"])
def loadTasteRecipe():
    queries = Recipe.query.order_by(func.rand()).limit(100).all()

    recipes = []

    for query in queries:
        recipe = convert_to_dict(query)
        recipes.append(recipe)

    return response_with_code("<success>", recipes)


@recipe_api.route("/signUp", methods=["POST"])
def signUp():
    signup_user = request.json

    if signup_user is None:
        return response_with_code("<fail>:1:no post data")

    query_result = RecipeUser.query.filter_by(email=signup_user["email"]).first()

    favor_recipe_id = signup_user["selectList"]
    favor_class_num = signup_user["selectClassList"]
    user_id = signup_user["id"]
    if query_result:
        return response_with_code("<fail>:2:already registered")

    user = RecipeUser(
        userID=signup_user["id"],
        nickName=signup_user["nickName"],
        email=signup_user["email"],
        gender=signup_user["gender"],
        profileUrl=signup_user["profileUrl"],
        ageRange=signup_user["ageRange"],
        allergy=signup_user["allergy"],
        disease=signup_user["disease"],
    )
    db.session.add(user)

    for id, num in zip(favor_recipe_id.split(" "), favor_class_num.split(" ")):
        history = ClickHistory(
            userID=signup_user["id"],
            recipeID=id,
            recipeClass=num,
        )
        db.session.add(history)

    db.session.commit()

    return response_with_code("<success>")


@recipe_api.route("/searchRecipe", methods=["GET"])
def searchRecipe():

    search_text = escape(request.args.get("recipeTitle")).strip()

    if not search_text or search_text == "":
        return response_with_code("<fail>:2:invalid search text")

    recipeList = Recipe.query.filter(Recipe.title.like("%" + search_text + "%")).limit(
        20
    )

    df = pd.read_sql(recipeList.statement, recipeList.session.bind)

    return response_with_code(
        "<success>", json.loads(df.to_json(orient="records", force_ascii=False))
    )


@recipe_api.route("/searchIngredient", methods=["GET"])
def searchIngredient():

    search_text = escape(request.args.get("ingredientName")).strip()

    if not search_text or search_text == "":
        return response_with_code("<fail>:2:invalid search text")

    ingredientList = Ingredient.query.filter(
        Ingredient.name.like("%" + search_text + "%")
    )

    df = pd.read_sql(ingredientList.statement, ingredientList.session.bind)

    return response_with_code(
        "<success>", json.loads(df.to_json(orient="records", force_ascii=False))
    )


@recipe_api.route("/updateDB", methods=["GET"])
def updateDB():
    df = pd.read_csv("/var/www/Appserver3/recipe.csv", encoding="utf-8")

    time = 0

    for index, item in tqdm(df.iterrows(), total=len(df)):
        try:
            recipe = Recipe(
                recipeID=int(item["recipe_id"]),
                className=str(item["class_name"]),
                classNum=int(item["class_num"]),
                title=str(item["title"]),
                ingredientList=str(item["ingred"]),
                author=str(item["author"]),
                time=str(item["time"]),
                level=str(item["level"]),
                photo=str(item["photo"]),
            )
            db.session.add(recipe)
            time += 1
            if time % 10000 == 0 or time == 105903:
                db.session.commit()
        except Exception:
            print("ERROR")

    return response_with_code("<success>")


@recipe_api.route("/updateDB2", methods=["GET"])
def updateDB2():
    df = pd.read_csv("/var/www/Appserver3/ingredient.csv", encoding="utf-8")

    for index, item in tqdm(df.iterrows(), total=len(df)):
        ingredient = Ingredient(name=item["0"], image=item["2"])
        db.session.add(ingredient)

    db.session.commit()
    return response_with_code("<success>")
