import json
import time
import pandas as pd
from pprint import pprint as pp

from flask import escape, Blueprint, request, session, current_app as app
from sqlalchemy import text

from extensions import *
from model import db, Recipe, RecipeUser, PostInfo, PostCommentInfo

from tqdm import tqdm

recipe_api = Blueprint("recipe", __name__, url_prefix="/recipe")


df = pd.read_csv("../samples.csv")

for index, item in tqdm(df.iterrows()):
    recipe = Recipe(recipeID=item["recipeID"],
                    cat1=item["cat1"],
                    cat2=item["cat2"],
                    title=item["title"],
                    ingredientList=item["ingredientList"],
                    ingredientAmount=item["ingredientAmount"],
                    author=item["author"],
                    time=item["time"],
                    level=item["level"],
                    photo=item["photo"],
                    )
    db.add(recipe)

db.commit()
print(done)
