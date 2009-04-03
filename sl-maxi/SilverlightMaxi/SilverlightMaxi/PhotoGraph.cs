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
		private List<Photo> photoList;
		private int numToDisplay;
		private int currentPointer = 0;
		Dictionary<String, int> edges = new Dictionary<String, int>();
		Dictionary<long, Node> nodes = new Dictionary<long, Node>();
		
		public PhotoGraph(List<Photo> pl, int toDisplay, Dictionary<long, Node> ns){
			photoList = pl;
			numToDisplay = toDisplay;
			nodes = ns;
		}
		
		//decrement edge values in dictionary by 1, if 0 then remove from graph
		private void remove(Photo photo, Graph g){
            for (int i = 0;  i<photo.getTaggedList().Count(); i++)
            {
                for (int j = i + 1; j < photo.getTaggedList().Count(); j++)
                {
                    long uid1 = photo.getTaggedList().ElementAt(i);
                    long uid2 = photo.getTaggedList().ElementAt(j);
                    String key = uid1 + "," + uid2;
                    if (!edges.ContainsKey(key))
                    {
                        edges.Add(key, 1);
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
                    edges.Add(key, edges[key] - 1);
                }
            }
		}
		//increment edge values in dictionary by 1, if starting at 0 or not existing then add to graph
		private void add(Photo photo, Graph g){
            for (int i = 0; i < photo.getTaggedList().Count(); i++)
            {
                for (int j = i + 1; j < photo.getTaggedList().Count(); j++)
                {
                    long uid1 = photo.getTaggedList().ElementAt(i);
                    long uid2 = photo.getTaggedList().ElementAt(j);
                    String key = uid1 + "," + uid2;
                    if (!edges.ContainsKey(key))
                    {
                        edges.Add(key, 0);
                        g.Edges[nodes[uid1], nodes[uid2]] = true;
                    }
                    else if (edges[key] == 0)
                    {
                        g.Edges[nodes[uid1], nodes[uid2]] = true;
                    }
                    edges.Add(key, edges[key] + 1);
                }
            }
		}
		
		public void next(Graph g){
			if(currentPointer+numToDisplay>=photoList.Count()){
			 return;
			}
			else{
			  currentPointer++;
              Photo addPhoto = photoList.ElementAt(currentPointer + numToDisplay);
              Photo removePhoto = photoList.ElementAt(currentPointer - 1);
				remove(removePhoto, g);
				add(addPhoto, g);
			}
		}
		public void previous(Graph g){
			if(currentPointer<=0){
			 return;
			}		
			else{
				currentPointer--;
				Photo addPhoto = photoList.ElementAt(currentPointer);
				Photo removePhoto = photoList.ElementAt(currentPointer+numToDisplay+1);
				remove(removePhoto, g);
				add(addPhoto, g);
			}
    	}
	}
}