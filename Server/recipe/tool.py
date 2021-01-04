import os
import sys
import urllib.request
import json
from fatsecret import Fatsecret
from tqdm import tqdm
import pandas as pd
import matplotlib.pyplot as plt
from wordcloud import WordCloud
import numpy as np
import base64
from PIL import Image
import cv2
from collections import Counter


# 네이버 파파고 번역 api
def translate(word):
    client_id = "l84GX3EyER6GPhqOznzE"
    client_secret = "Rb1fwwcZSc"
    encText = urllib.parse.quote(word)
    data = "source=ko&target=en&text=" + encText
    url = "https://openapi.naver.com/v1/papago/n2mt"
    request = urllib.request.Request(url)
    request.add_header("X-Naver-Client-Id", client_id)
    request.add_header("X-Naver-Client-Secret", client_secret)
    response = urllib.request.urlopen(request, data=data.encode("utf-8"))
    rescode = response.getcode()
    if rescode == 200:
        response_body = response.read().decode("utf-8")
        tmp = json.loads(response_body)
        return tmp["message"]["result"]["translatedText"]
    else:
        print("Error Code:" + rescode)


def getCalorie(recipeID):
    fs = Fatsecret(
        "9b00b592903147709dc78f969353a598", "d2d2abbf973d453388512a33dd036f4a"
    )

    df = pd.read_csv("/var/www/Appserver3/recipe/cat2_eng.csv")

    word = df[df.id == int(recipeID)].cat2.values[0]
    # 영어로 바꿔서
    search_target = translate(word)

    # fatsecret에 검색
    foods = fs.foods_search(search_target)

    # 검색 결과가 많아 검색결과중 첫번째를 선택
    nut = foods[0]["food_description"]
    nut = nut.split("|")
    nut[0] = nut[0].split("-")[1]
    nutrient = {}
    for b in nut:
        i = b.split(":")
        i[0] = i[0].strip()
        nutrient[i[0]] = i[1].strip()

    # # 칼로리, 탄수, 지방, 단백질
    # print(nutrient["Calories"])
    # print(nutrient["Carbs"])
    # print(nutrient["Fat"])
    # print(nutrient["Protein"])

    return [
        nutrient["Calories"],
        nutrient["Carbs"],
        nutrient["Fat"],
        nutrient["Protein"],
    ]


def generateWordCloud(word_list, userID):

    image_dir = "/var/www/Appserver3/recipe/wc_image/" + userID + ".png"

    word_list = Counter(word_list)

    mask = np.array(Image.open("/var/www/Appserver3/recipe/chef.png"))

    wc = WordCloud(
        font_path="/var/www/Appserver3/recipe/NanumSquareRoundR.ttf",
        background_color="rgba(255, 255, 255, 0)",
        mode="RGBA",
        max_words=2000,
        mask=mask,
        max_font_size=100,
    )
    wc = wc.generate_from_frequencies(word_list)

    wc.to_file(image_dir)

    img_str = ""
    with open(image_dir, "rb") as img:
        img_str = base64.b64encode(img.read())

    with open("/var/www/Appserver3/recipe/wc_image/" + userID + ".txt", "w") as f:
        f.write(str(img_str))
