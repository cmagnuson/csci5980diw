#!/soft/python-2.5-bin/python

import os
import cgi
import cgitb; cgitb.enable()
import urllib2

def main():
    print "Content-Type: text/xml\r\n"
    url = os.getenv('QUERY_STRING')
    data = urllib2.urlopen(url).read()
    print data

main()
