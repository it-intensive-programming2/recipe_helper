# Appserver

[![Build Status](https://travis-ci.com/Algostu/Appserver.svg?token=G8pVCbCauf3DdVpT6k6s&branch=master)](https://travis-ci.com/Algostu/Appserver)
[![codecov](https://codecov.io/gh/Algostu/Appserver/branch/master/graph/badge.svg?token=Q60ZB3RKIR)](https://codecov.io/gh/Algostu/Appserver/)

## Structure  

1. main
  - __init__.py : app factory. Do not touch 
  - view.py : If you want to add new app, pls add your blue print here
  - config.py : dev, test, production config file
  
2. article
- view.py : api file

3. login 
- view.py : api file

4. test
- article_test.py : article을 test하기 위해 만든 테스트 코드
   
manage.py : manager와 orm을 등록 시켜 주는 곳 건들 필요 없음.

## How to add new url

If you want to add new url, pls add code sompe app/view.py. And If you want to write your test code for testing your own code. write test code in test/ folder. you can use base code.
