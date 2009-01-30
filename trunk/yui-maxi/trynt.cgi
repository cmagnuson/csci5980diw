#!/soft/python-2.5-bin/python
import cgitb; cgitb.enable()
import os
import urllib

url = 'http://www.trynt.com/movie-boxofficemojo-similar-api/v1/?fo=json&t=' + os.getenv('QUERY_STRING')
json_string = urllib.urlopen(url).read()

print "Content-Type: text/plain\r\n"
print json_string
