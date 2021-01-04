import os
import pytest
import json
import tempfile
import requests

article_id = 0

def pytest_generate_tests(metafunc):
    # called once per each test function
    funcarglist = metafunc.cls.params[metafunc.function.__name__]
    argnames = sorted(funcarglist[0])
    metafunc.parametrize(
        argnames, [[funcargs[name] for name in argnames] for funcargs in funcarglist]
    )

class TestArticle:
    # a map specifying multiple argument sets for a test method
    params = {
        "test_read_article": [dict(params="articleID=20&articleType=2", ev=dict(articleID=20, articleType=2))],
        "test_write_article": [dict(body={"articleType": 1,"userId": 1,"isAnonymous": True,"title" : "Troll","content": "hello"})],
        "test_delete_article": [dict(params="userID=2&articleID={0}&articleType=2")],
        "test_get_article_list": [dict(params="articleType=2&articleTime=latest")]
    }

    def test_read_article(self, params, ev):
        URL = 'http://49.50.164.11:5000/article/read'
        response = requests.get(URL, params=params)
        rv = response.json()
        assert rv['articleID'] == ev['articleID']

    def test_write_article(self, body):
        URL = 'http://49.50.164.11:5000/article/write'
        response = requests.post(URL, json=body)
        rv = response.text
        article_id = rv.split(':')[1]
        assert 'success' in rv

    def test_delete_article(self, params):
        URL = 'http://49.50.164.11:5000/article/delete'
        response = requests.get(URL, params=params.format(article_id))
        rv = response.text
        assert 'success' in rv

    def test_get_article_list(self, params):
        URL = 'http://49.50.164.11:5000/article/articleList'
        response = requests.get(URL, params=params)
        rv = response.json()
        assert len(rv) > 0

    # def test_messages(self):
    #     self.login('admin', 'default')
    #     rv = self.app.post('/add', data=dict(
    #         title='<Hello>',
    #         text='<strong>HTML</strong> allowed here'
    #     ), follow_redirects=True)
    #     assert b'No entries here so far' not in rv.data
    #     assert b'&lt;Hello&gt;' in rv.data
    #     assert b'<strong>HTML</strong> allowed here' in rv.data

    # def login(self, username, password):
    #     return self.app.post('/login', data=dict(
    #         username=username,
    #         password=password
    #     ), follow_redirects=True)
    #
    # def logout(self):
    #     return self.app.get('/logout', follow_redirects=True)
    #
    # def test_login_logout(self):
    #     rv = self.login('admin', 'default')
    #     assert b'You were logged in' in rv.data
    #     rv = self.logout()
    #     assert b'You were logged out' in rv.data
    #     rv = self.login('adminx', 'default')
    #     assert b'Invalid username' in rv.data
    #     rv = self.login('admin', 'defaultx')
    #     assert b'Invalid password' in rv.data
