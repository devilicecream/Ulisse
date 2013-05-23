__author__ = 'walter'

from datetime import datetime
from flask import Flask, request, flash, url_for, redirect, render_template, abort
from flask.ext.sqlalchemy import SQLAlchemy


app = Flask(__name__)
app.config.from_pyfile('conf.ini')
DBSession = SQLAlchemy(app)


if __name__ == '__main__':
    from model import *
    from api.apis import *

    DBSession.create_all()

    app.run(host='0.0.0.0', port=5000)
