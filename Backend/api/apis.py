__author__ = 'walter'

from flask import Flask, request, flash, url_for, redirect, render_template, abort, make_response
from ulisse import app, DBSession
import json
from model import User

@app.after_request
def jsonify(response):
    response.headers['Content-type'] = 'application/json'
    return response

@app.route('/test')
def test():
    return json.dumps(dict(request.args))

@app.route('/login', methods=['POST', 'GET'])
def login(fb_id=None, fb_token=None, gp_id=None, gp_token=None, access_token=None):

    return ""
