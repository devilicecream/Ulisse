__author__ = 'walter'

from pygeocoder import Geocoder

def get_coords(address):
    results = Geocoder.geocode(address)
    return(results[0].coordinates)