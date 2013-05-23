
import csv
import json
import urllib2
import re
import sys
from pprint import pprint

POINT_RE = re.compile(r'^\s*POINT\((\d+\.\d+)\s+(\d+\.\d+)\)\s*$')
def parse_point(s):
	if s != None:
		match = POINT_RE.match(s)
		if match is not None:
			return [ float(valstr) for valstr in match.groups() ]
	return None

class dialect(csv.excel):
	delimiter = ';'

def parse(in_stream, category):
	csv_reader = csv.reader(in_stream, dialect=dialect)
	headers = [ h.lower() for h in next(csv_reader) ]

	rows = []
	for row in csv_reader:
		data_in = dict(zip(headers, row))
		pprint(data_in)

		point = parse_point(data_in['wkt_geom'])
		nome = data_in.get('denominazi') or \
				data_in.get('denominaz') or \
				data_in.get('denominazione')
		address = data_in.get('indirizzo') or ''
		data_out = {
			'pos_lat': point[0],
			'pos_lon': point[1],
			'name': nome,
			'address': address,
			'category': category
		}
		rows.append(data_out)
			
	return rows

class DatiPiemonte:
	def __init__(self, datasets=[]):
		self.datasets = datasets

	def add_dataset(self, dataset, category):
		self.datasets.append((dataset, category))

	def __call__(self):
		rows = []
		for filename, category in self.datasets:
			print "// File: %s" % filename

			url = "http://www.comune.torino.it/aperto/bm~doc/%s.csv" % filename
			in_stream = urllib2.urlopen(url)
			rows += parse(in_stream, category=category)
			in_stream.close()
		return rows


