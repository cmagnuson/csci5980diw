using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.IO;
using System.Linq;
using System.Xml;
using System.Xml.Linq;
using System.Collections.Generic;
using System.Collections;

namespace SilverlightMaxi
{

    public class Photo
    {
        private long pid;
        private long time;
        private long taker;
        private String url;
        private IEnumerable<long> taggedList;

        public void setTaggedList(IEnumerable<long> taggedList)
        {
            this.taggedList = taggedList;
        }
        public IEnumerable<long> getTaggedList()
        {
            return taggedList;
        }
        public void setPid(long pid)
        {
            this.pid = pid;
        }
        public long getPid()
        {
            return pid;
        }
        public void setTime(long time)
        {
            this.time = time;
        }
        public long getTime()
        {
            return time;
        }
        public void setTaker(long taker)
        {
            this.taker = taker;
        }
        public long getTaker()
        {
            return taker;
        }
        public void setUrl(String url)
        {
            this.url = url;
        }
        public String getUrl()
        {
            return url;
        }

        static public IOrderedEnumerable<Photo> getPhotoList(String filename, Object o)
        {
            string graphResource = filename;
            //string graphResource = "Graphite.Demo.friends.xml";
            System.IO.Stream s = o.GetType().Assembly.GetManifestResourceStream(graphResource);

            XDocument xml = XDocument.Load(XmlReader.Create(s));
            XElement elementRoot = xml.Root; // Not sure if this is necessary
            var elements = from element in elementRoot.Descendants("photo")
                           select new
                           {
                               pid = (long)element.Element("photoid"),
                               time = (long)element.Element("time"),
                               taker = (long)element.Element("taker"),
                               url = (String)element.Element("url"),
                               taggedlist = from friend in element.Element("tagged").Descendants()
                                            select (long)element.Element("uid"),
                           };

            List<Photo> list = new List<Photo>();
            foreach (var el in elements)
            {
                Photo p = new Photo();
                p.setPid(el.pid);
                p.setTaker(el.taker);
                p.setUrl(el.url);
                p.setTime(el.time);
                p.setTaggedList(el.taggedlist);
                list.Add(p);
            }
            return list.OrderBy(p => p.getTime());
            
//            return list.Values.ToList();// ToList();
        }
    }
}