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

    public class PhotoGraph
    {
		private IOrderedEnumerable<Photo> photoList;
		private int numToDisplay;
		private int currentPointer = 0;
		Dictionary<String, int> edges = new Dictionary<String, int>();
		Dictionary<long, Node> nodes = new Dictionary<long, Node>();
		
		public PhotoGraph(IOrderedEnumerable<Photo> pl, int toDisplay, Dictionary<long, Node> ns){
			photoList = pl;
			numToDisplay = toDisplay;
			nodes = ns;
		}
		
		//decrement edge values in dictionary by 1, if 0 then remove from graph
		private void remove(Photo photo, ref Graph g){
            for (int i = 0;  i<photo.getTaggedList().Count(); i++)
            {
                for (int j = i + 1; j < photo.getTaggedList().Count(); j++)
                {
                    long uid1 = photo.getTaggedList().ElementAt(i);
                    long uid2 = photo.getTaggedList().ElementAt(j);
                    String key = uid1 + "," + uid2;
                    if (!nodes.ContainsKey(uid1) || !nodes.ContainsKey(uid2))
                    {
                        continue;
                    }
                    else if (edges[key] == 1)
                    {
                        g.Edges[nodes[uid1], nodes[uid2]] = false;
                    }
                    else if (edges[key] < 1)
                    {
                        //should never happen!
                        edges.Add(key, 1);
                    }
                    edges[key]--;
                }
            }
		}
		//increment edge values in dictionary by 1, if starting at 0 or not existing then add to graph
		private void add(Photo photo, ref Graph g){
            for (int i = 0; i < photo.getTaggedList().Count(); i++)
            {
                for (int j = i + 1; j < photo.getTaggedList().Count(); j++)
                {
                    long uid1 = photo.getTaggedList().ElementAt(i);
                    long uid2 = photo.getTaggedList().ElementAt(j);
                    String key = uid1 + "," + uid2;
                    if (!nodes.ContainsKey(uid1) || !nodes.ContainsKey(uid2))
                    {
                        continue;
                    }
                    if(!g.Nodes.Contains(nodes[uid1])){
                        g.Nodes.Add(nodes[uid1]);
                    }
                    if(!g.Nodes.Contains(nodes[uid2])){
                        g.Nodes.Add(nodes[uid2]);
                    }
                    if (!edges.ContainsKey(key))
                    {
                        edges.Add(key, 1);
                        g.Edges[nodes[uid1], nodes[uid2]] = true;
                    }
                    else if (edges[key] == 0)
                    {
                        edges[key] = edges[key] + 1;
                        g.Edges[nodes[uid1], nodes[uid2]] = true;
                    }
                    else
                    {
                        edges[key]++;
                    }
                }
            }
		}

        public void addInitial(ref Graph g)
        {
            for (int i = 0; i < numToDisplay; i++)
            {
                currentPointer++;
                add(photoList.ElementAt(i), ref g);
            }
        }

        public void next(ref Graph g)
        {
			if(currentPointer+numToDisplay>=photoList.Count()){
			 return;
			}
			else{
			  currentPointer++;
              Photo addPhoto = photoList.ElementAt(currentPointer + numToDisplay);
              if (currentPointer + numToDisplay - 1 >= 0)
              {
                  Photo removePhoto = photoList.ElementAt(currentPointer - 1);
                  remove(removePhoto, ref g);
              }
				add(addPhoto, ref g);
			}
		}
		public void previous(ref Graph g){
			if(currentPointer<=0){
			 return;
			}		
			else{
				currentPointer--;
				Photo addPhoto = photoList.ElementAt(currentPointer);
				Photo removePhoto = photoList.ElementAt(currentPointer+numToDisplay+1);
				remove(removePhoto, ref g);
				add(addPhoto, ref g);
			}
    	}
	}
}