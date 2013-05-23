__author__ = 'walter'

from pygeocoder import Geocoder
import time

def get_coords(address):
    time.sleep(0.5)
    try:
        address = address.replace("'", "")
        results = Geocoder.geocode(address)
    except:
        print "\n\n\nError happened\n\n\n"
        return None, None
    return results[0].coordinates

def get_area(lat, lng, distance):
    distance /= 500.0
    lat1 = lat - distance
    lat2 = lat + distance

    lng1 = lng - distance
    lng2 = lng + distance

    print (lat1, lng1), (lat2, lng2)
    return (lat1, lng1), (lat2, lng2)