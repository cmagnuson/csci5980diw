#!/soft/python-2.5-bin/python
from simplejson.encoder import JSONEncoder
import BeautifulSoup
import urllib
import os
import cgitb; cgitb.enable()

print "Content-Type: text/plain\r\n"

url = 'http://www.boxofficemojo.com/movies/?page=daily&view=chart&id=%s.htm' % os.getenv('QUERY_STRING')
html_string = urllib.urlopen(url).read()
soup = BeautifulSoup.BeautifulSoup(html_string)
try:
    table = soup.find("tr", {"bgcolor": "#f4f4ff"}).findParent("table")
except AttributeError:
    print JSONEncoder().encode({'results': []})
    import sys; sys.exit()

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
    if num == "n/a": return None
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

data = {
    'results': newlist,
    }

print JSONEncoder().encode(data)
