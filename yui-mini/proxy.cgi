    #!/soft/python-2.5-bin/python

    import os
    import cgi
    import cgitb; cgitb.enable()
    import urllib2

    def main():
        print "Content-Type: text/xml\r\n"
        url = 'http://ecs.amazonaws.com/onca/xml?' + os.getenv('QUERY_STRING')
            data = urllib2.urlopen(url).read()
            print data

    main()
