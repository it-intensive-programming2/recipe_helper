# python basic library
import os

# external package
from flask_security import SQLAlchemyUserDatastore
from flask import Flask
from sqlalchemy import create_engine, text
from main.model import *
from flask_admin import helpers as admin_helpers
from flask import Flask, url_for, redirect, render_template, request, abort

template_dir = os.path.dirname(os.path.abspath(os.path.dirname(__file__)))
template_dir = os.path.join(template_dir, "templates")

## __init__ files:
## 1. bycrypt 실행 하기 : hash 코드 생성하기
## 2. create_app 함수와 같은 클래스, 함수 같은 것들 만들어 놓기
## 3. 임포트 제한 (X)


def create_app(config_name):
    """flask application factory
    args:
        config_name = configuration mode
    """
    # import configuration file
    from . import config
    from .view import api_urls
    from .model import db
    from .extensions import flask_bcrypt, login_manager, sess, admin, security, mail

    # create and configure the app
    app = Flask(__name__, instance_relative_config=True, template_folder=template_dir)
    app.config.from_object(config.config_by_name[config_name])

    # apply admin page
    admin.init_app(app)

    # apply url
    for url in api_urls:
        app.register_blueprint(url)

    # apply db
    db.init_app(app)
    app.db = create_engine(app.config["SQLALCHEMY_DATABASE_URI"])

    # # apply redis session interface
    # sess.init_app(app)

    # apply crypto method
    flask_bcrypt.init_app(app)

    # apply model
    user_datastore = SQLAlchemyUserDatastore(db, RecipeUser, Role)

    # apply Security
    secur = security.init_app(app, user_datastore)

    @secur.context_processor
    def security_context_processor():
        return dict(
            admin_base_template=admin.base_template,
            admin_view=admin.index_view,
            h=admin_helpers,
            get_url=url_for,
        )

    # apply Mail
    mail.init_app(app)

    return app
