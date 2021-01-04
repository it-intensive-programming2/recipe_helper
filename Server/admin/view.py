import json, time, os, sys
from datetime import datetime
from flask_security import Security,  \
    UserMixin, RoleMixin, login_required, current_user
from flask import Flask, url_for, flash, url_for, redirect, render_template, request, abort
from flask import jsonify, make_response, escape, Blueprint, session, current_app as app
from sqlalchemy import text, desc
from main.extensions import *
from main.model import *
from flask_admin.contrib import sqla
from flask_admin import Admin, BaseView, expose
from flask import Blueprint

parent_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, parent_dir)

admin_api = Blueprint('superUser', __name__, url_prefix='/')

# admin.add_view(ModelView(UserInfo, db.session))


class MyModelView(sqla.ModelView):
    def is_accessible(self):
        return (current_user.is_active and
                current_user.is_authenticated and
                current_user.has_role('superuser')
        )

    def _handle_view(self, name, **kwargs):
        """
        Override builtin _handle_view in order to redirect users when a view is not accessible.
        """
        if not self.is_accessible():
            if current_user.is_authenticated:
                # permission denied
                abort(403)
            else:
                # login
                return redirect(url_for('security.login', next=request.url))

class MicroBlogModelView(MyModelView):
    can_delete = False  # disable model deletion
    page_size = 50  # the number of entries to display on the list view
    can_create = True

class UserControl(MicroBlogModelView):
    # column_filters = ['schoolID', 'regionID']
    column_editable_list = ['authorized']
    column_searchable_list = ['studentName', 'schoolName']
    column_filters = ['studentName', 'schoolName', 'authorized', 'signupDate']
    can_delete = True
    create_modal = True
    edit_modal = True
    can_export = True
    # inline_models = (SchoolInfo,)
    # inline_models = (SchoolInfo,)


admin.add_view(UserControl(UserInfo, db.session, endpoint='acces/control', category="Auth"))
admin.add_view(MicroBlogModelView(ArticleAll, db.session, endpoint='article/control', category="Article"))

# Flask views
@admin_api.route('/')
def index():
    return render_template('index.html')

admin.add_view(MyModelView(Role, db.session))
admin.add_view(MyModelView(WebUser, db.session))
