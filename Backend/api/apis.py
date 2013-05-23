__author__ = 'walter'

from flask import Flask, request, flash, url_for, redirect, render_template, abort, make_response, send_from_directory
from ulisse import app, DBSession
import json, hashlib
from model import User, Place, Document
import uuid, os

RES_TYPE_PHOTO = 1
RES_TYPE_TEXT = 2

# ALLOWED_EXTENSIONS = {
# 	RES_TYPE_PHOTO: ['.jpg', '.bmp'],
# 	'photo': ['.jpg', '.bmp'],
# }

def error_unauthorized():
    error = dict(error='Unauthorized', state=401)
    return json.dumps(error) #, 401, {}

def error_invalid_value(what, wrong_val=None):
    errstr = 'Invaild value for "%s"' % what
    if wrong_val: errstr += " (was: "+str(wrong_val)+")"
    error = dict(error=errstr, state=401)
    return json.dumps(error) #, 401, {}

def error_missing_param(what):
    errstr = 'Missing parameter in request (%s)' % what
    error = dict(error=errstr, state=400)
    return json.dumps(error), # 401, {}

def error_invalid_id(what, wrong_id=None):
    errstr = 'Invalid id for %s' % what
    if wrong_id: errstr += ' (wrong id: %d)' % wrong_id
    error = dict(error=errstr, state=400) # bad request
    return json.dumps(error), # 401, {}


def validate_couple(first, second):
    if first and second:
        return first, second
    return None

def verify_filename(res_type, filename):
    ## STUB!
    return True
    '''return '.' in filename and \
        filename.lower().rsplit('.', 1)[1] in \
    	    ALLOWED_EXTENSIONS.get(res_type, [])'''

def verify_login(token):
    return DBSession.session.query(User.access_token).first() != None

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

        return error_unauthorized()
    auth, auth_type = (gp_auth, 'gp') if gp_auth else (fb_auth, 'fb')

    if auth_type == 'gp':
        user = DBSession.query(User).filter_by(gp_id = auth[0], gp_token = auth[1]).first()
    else:
        user = DBSession.query(User).filter_by(fb_id = auth[0], fb_token = auth[1]).first()
    if not access_token and user:
        return error_unauthorized()
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
    area = None#get_area(lat, long, 10)
    print area
    places = DBSession.query(Place).filter('pos_lat' >= area[0][0], 'pos_long' >= area[0][1],
                                           'pos_lat' <= area[1][0], 'pos_long' <= area[1][1]).all()

    return ""

@app.route('/uploads/<filename>')
def uploaded_file(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'],
                               filename)

@app.route('/upload', methods=['POST'])
def upload():
    session = DBSession.session

    try:
        f = request.files['file']
        res_type = request.form['res_type']
        name = request.form['name']
        place_id = request.form['place_id']
        token = request.form['access_token']
    except KeyError:
        return error_missing_param('<unknown>') # TODO report missing key name

    if not verify_login(token):
        return error_unauthorized()

    if res_type == 'photo':
        res_type = RES_TYPE_PHOTO
    elif res_type == 'text':
        res_type = RES_TYPE_TEXT
    else:
        return error_invalid_value('res_type', res_type)

    # TODO Infer filename
    if not verify_filename(res_type, f.filename):
        return error_invalid_value('file', f.filename)

    filename = str(uuid.uuid4())
    if f.filename.lower().endswith('jpg'):
        filename += ".jpg"
    elif f.filename.lower().endswith('.bmp'):
        filename += ".bmp"

    out_path = os.sep.join(( app.config['UPLOAD_FOLDER'], filename ))
    print " ~ Will save upload to", out_path

    place = session.query(Place).filter_by(uid = place_id).first()
    if place is None:
        return error_invalid_id('place', place_id)

    doc = Document()
    doc.name = name
    doc.url = url_for('uploaded_file', filename=filename)
    doc.res_type = res_type
    doc.place = place
    session.add(doc)
    session.commit()

    f.save(out_path)

    ret = { 'url': doc.url, 'id': doc.uid }
    return json.dumps(ret)


