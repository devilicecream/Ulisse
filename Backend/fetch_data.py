#!/usr/bin/python

from utils import *
import json
import ulisse
from model import *

FETCHERS = [
	DatiPiemonte([
		("musei_2010", 'museo'),
		("cinema_2011", 'cinema'),
		("teatri_2010", 'teatro'),
		# ("luoghi_locali_storici_2010", 'luogo_storico'),
		("biblioteche_2010", 'biblioteca')
		 ])
	]

if __name__ == '__main__':
	session = ulisse.DBSession.session
	
	USERNAME = "open_data"
	user = session.query(User).filter_by(name=USERNAME).first()
	if user == None:
		user = User(name=USERNAME, rating=200)
		session.add(user)
		session.commit()

	cats = {}
	for cat in session.query(Category):
		cats[str(cat.name)] = cat

	new_cats = {}

	rows = []
	for fetcher in FETCHERS:
		rows += fetcher()
	
	for row in rows:
		cat_name = row['category']
		cat = session.query(Category).filter_by(name=cat_name).first()
		if cat == None:
			cat = Category()
			cat.name = cat_name
			session.add(cat)
			session.commit()

		record = Place()
		record.name = row['name']
		record.address = row['address']
		record.pos_lat = row['pos_lat']
		record.pos_lon = row['pos_lon']
		record.category = cat
		record.user = user
		session.add(record)

	session.commit()

