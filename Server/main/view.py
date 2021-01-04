from flask import Blueprint
from recipe.view import recipe_api

main_api = Blueprint("main", __name__, url_prefix="/")


@main_api.route("/index", methods=["GET"])
def main():
    return "hello"


api_urls = [
    main_api,
    recipe_api,
]
