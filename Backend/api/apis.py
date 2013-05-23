__author__ = 'walter'

from flask import Flask, request, flash, url_for, redirect, render_template, abort, make_response
from ulisse import app, DBSession
import json, hashlib
from model import User

class Error():
    @classmethod
    def unauthorized(cls):
        error = dict(error='Unauthorized', state=401)
        return json.dumps(error), 401, {}

def validate_couple(first, second):
    if first and second:
        return first, second
    return None

@app.after_request
def jsonify(response):
    response.headers['Content-type'] = 'application/json'
    return response

@app.route('/test')
def test():
    return json.dumps(dict(request.args))

@app.route('/login', methods=['POST', 'GET'])
def login(fb_id=None, fb_token=None, gp_id=None, gp_token=None, access_token=None):
    fb_auth = validate_couple(fb_id, fb_token)
    gp_auth = validate_couple(gp_id, gp_token)
    if not fb_auth and not gp_auth:
        return Error.unauthorized()
    auth, auth_type = (gp_auth, 'gp') if gp_auth else (fb_auth, 'fb')

    if auth_type == 'gp':
        user = DBSession.query(User).filter_by(gp_id = auth[0], gp_token = auth[1]).first()
    else:
        user = DBSession.query(User).filter_by(fb_id = auth[0], fb_token = auth[1]).first()
    if not access_token and user:
        return Error.unauthorized()
    else:
        if not access_token:
            token = hashlib.sha1(str(auth)).hexdigest()
            user = User(access_token=token)
            if auth_type == 'gp':
                user.gp_id = auth[0]
                user.gp_token = auth[1]
            else:
                user.fb_id = auth[0]
                user.fb_token = auth[1]
            DBSession.session.add(user)
            DBSession.session.commit()
        user_info = dict(id = user.id, access_token = user.access_token)
    return user_info
