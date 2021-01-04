import os
from glob import glob
from pprint import pprint as pp
import random
import pandas as pd
import re
import json


df = pd.read_csv("recipe.csv", encoding="utf-8")
df_ingred = pd.read_csv("ingredient.csv", encoding="cp949")

ingredient_list = ["콩나물", "김치", "파슬리"]

total = len(ingredient_list)
find_id = []

for row, id in zip(df.ingred, df.recipe_id):
    found = 0
    for ingredient in ingredient_list:
        if ingredient in row:
            found += 1

    if found == total:
        find_id.append(int(id))

delete_list = []
# print(len(find_id))

while len(find_id) < 15 and len(ingredient_list) > 1:
    cnt_list = []

    for ingredient in ingredient_list:
        cnt = df_ingred.loc[df_ingred["0"] == ingredient, "1"].values[0]
        cnt_list.append(cnt)

    min_index = cnt_list.index(min(cnt_list))
    # print(ingredient_list)
    # print("DELETE ", ingredient_list[min_index])
    delete_list.append(ingredient_list[min_index])
    ingredient_list.remove(ingredient_list[min_index])

    total = len(ingredient_list)
    for row, id in zip(df.ingred, df.recipe_id):
        found = 0
        for ingredient in ingredient_list:
            if ingredient in row:
                found += 1

        if found == total:
            find_id.append(int(id))
    # print("LENGTH: ", len(find_id))

# print("find id: ", len(find_id))