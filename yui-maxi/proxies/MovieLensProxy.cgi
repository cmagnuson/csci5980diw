#!/soft/python-2.5-bin/python

import os
import cgi
import cgitb; cgitb.enable()
import urllib2

def main():
    print "Content-Type: application/json\r\n"
    url = 'http://frost.cs.umn.edu/morten/movielenstitle.php?' + os.getenv('QUERY_STRING')
    data = urllib2.urlopen(url).read()
    print data

main()
