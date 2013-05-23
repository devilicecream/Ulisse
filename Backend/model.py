__author__ = 'walter'

from ulisse import DBSession
from datetime import datetime


class User(DBSession.Model):
    __tablename__ = 'users'
    uid = DBSession.Column('uid', DBSession.Integer, primary_key=True, autoincrement=True)
    name = DBSession.Column(DBSession.String(60))
    access_token = DBSession.Column(DBSession.String(200), default="")

    fb_id = DBSession.Column(DBSession.String(60), default="")
    fb_token = DBSession.Column(DBSession.String(200), default="")

    gp_id = DBSession.Column(DBSession.String(60), default="")
    gp_token = DBSession.Column(DBSession.String(200), default="")

    rating = DBSession.Column(DBSession.Integer, default=0)


class Place(DBSession.Model):
    __tablename__ = 'places'
    uid = DBSession.Column('uid', DBSession.Integer, primary_key=True, autoincrement=True)
    name = DBSession.Column(DBSession.String(60))
    address = DBSession.Column(DBSession.String(150))
    pos_lat = DBSession.Column(DBSession.Float, default=0)
    pos_lon = DBSession.Column(DBSession.Float, default=0)

    up = DBSession.Column(DBSession.Integer, default=0)
    down = DBSession.Column(DBSession.Integer, default=0)

    user_id = DBSession.Column(DBSession.Integer, DBSession.ForeignKey('users.uid'))
    user = DBSession.relationship('User', backref=DBSession.backref('places', lazy='dynamic'))

    category_id = DBSession.Column(DBSession.Integer, DBSession.ForeignKey('categories.uid'))
    category = DBSession.relationship('Category', backref=DBSession.backref('places', lazy='dynamic'))


class Documents(DBSession.Model):
    __tablename__ = 'documents'
    uid = DBSession.Column('uid', DBSession.Integer, primary_key=True, autoincrement=True)
    name = DBSession.Column(DBSession.String(60))
    url = DBSession.Column(DBSession.String(200), default="")

    up = DBSession.Column(DBSession.Integer, default=0)
    down = DBSession.Column(DBSession.Integer, default=0)

    place_id = DBSession.Column(DBSession.Integer, DBSession.ForeignKey('places.uid'))
    place = DBSession.relationship('Place', backref=DBSession.backref('documents', lazy='dynamic'))


class Category(DBSession.Model):
    __tablename__ = 'categories'
    uid = DBSession.Column('uid', DBSession.Integer, primary_key=True, autoincrement=True)
    name = DBSession.Column(DBSession.String(60))

