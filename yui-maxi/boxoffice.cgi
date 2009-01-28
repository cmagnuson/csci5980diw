#!/soft/python-2.5-bin/python
from simplejson.decoder import JSONDecoder
from simplejson.encoder import JSONEncoder
import BeautifulSoup
import urllib
import os
import cgitb; cgitb.enable()

url = 'http://www.trynt.com/movie-boxofficemojo-similar-api/v1/?fo=json&t=' + os.getenv('QUERY_STRING')
json_string = urllib.urlopen(url).read()
data = JSONDecoder().decode(json_string)
movie_id = data[u'trynt'][u'movie-boxoffice'][u'matched-id']

url = 'http://www.boxofficemojo.com/movies/?page=daily&view=chart&id=%s.htm' % movie_id
html_string = urllib.urlopen(url).read()
soup = BeautifulSoup.BeautifulSoup(html_string)
table = soup.find("tr", {"bgcolor": "#f4f4ff"}).findParent("table")

def removeextraspaces(string):
    while '  ' in string:
        string = string.replace('  ', ' ')
    return string.strip()
def html2text(node):
    if not hasattr(node, 'contents'):
        return node.replace('\n', ' ').replace('&nbsp;', ' ')
    if node.isSelfClosing:
        return ' '
    return ''.join([html2text(x) for x in node.contents])
def content(array):
    return [removeextraspaces(html2text(x)) for x in array]
def table2list(table):
    return [content(row.findChildren('td')) for row in table.findChildren('tr')]

data = table2list(table)

#clean data
def clean_int(num):
    return int(num.replace('$','').replace(',','').replace('%',''))
newlist = []
for line in data:
    if line[0] == 'Day' or line == ['']:
        #skip header line and blank lines
        continue
    newlist.append({
        'day': line[0],
        'date': line[1],
        'gross': clean_int(line[3]),
        'theaters': clean_int(line[6]),
        'avg': clean_int(line[7]),
        'grosstd': clean_int(line[8]),
        'daynum': clean_int(line[9]),
        })

print "Content-Type: text/plain\r\n"
print JSONEncoder().encode(newlist)
