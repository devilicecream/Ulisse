__author__ = 'walter'

from flask import Flask, request, flash, url_for, redirect, render_template, abort, make_response
from ulisse import app, DBSession
import json, hashlib
from model import User, Place

class Error():
    @classmethod
    def unauthorized(cls):
        error = dict(error='Unauthorized', state=401)
        return json.dumps(error)

    @classmethod
    def wrong_params(cls):
        error = dict(error="Wrong params", state=400)
        return json.dumps(error)


def validate_couple(first, second):
    if first and second:
        return first, second
    return None

@app.after_request
def jsonify(response):
    response.headers['Content-type'] = 'application/json'
    print response.response
    return response

@app.route('/test')
def test():
    return json.dumps(dict(request.args))

@app.route('/login', methods=['GET', 'POST'])
def login():
    gp_id = request.form.get('gp_id')
    fb_id = request.form.get('fb_id')
    gp_token = request.form.get('gp_token')
    fb_token = request.form.get('fb_token')
    access_token = request.form.get('access_token')

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



@app.route('/get_places', methods=['POST', 'GET'])
def get_places():
    lat = float(int(request.form.get('lat', 0)))
    long = float(int(request.form.get('lon', 0)))
    area = get_area(lat, long, 10)
    print area
    places = DBSession.query(Place).filter('pos_lat' >= area[0][0], 'pos_long' >= area[0][1],
                                           'pos_lat' <= area[1][0], 'pos_long' <= area[1][1]).all()

    return ""


