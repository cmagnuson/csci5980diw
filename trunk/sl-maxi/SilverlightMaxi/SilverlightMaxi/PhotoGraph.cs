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
		public IOrderedEnumerable<Photo> photoList;
		public int numToDisplay;
		public int currentPointer = 0;
		Dictionary<String, int> edges = new Dictionary<String, int>();
		public Dictionary<long, Node> nodes = new Dictionary<long, Node>();
        public GraphViewer viewer;
		
		
		//decrement edge values in dictionary by 1, if 0 then remove from graph
		private void remove(Photo photo){
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
                    if (!edges.ContainsKey(key)) {
                        throw new Exception("Removing edge that does not exist");
                    }
                    else if (edges[key] == 1)
                    {
                        viewer.RemoveEdge(nodes[uid1], nodes[uid2]);
                    }
                    else if (edges[key] < 1)
                    {
                        throw new Exception("Edge Does Not Exist");
                    }
                    edges[key]--;
                }
            }
		}
		//increment edge values in dictionary by 1, if starting at 0 or not existing then add to graph
		private void add(Photo photo){
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
                    try
                    {
                        viewer.AddNode(nodes[uid1]);
                    }
                    catch (Graph.NodeAlreadyExists e) { }
                    try
                    {
                        viewer.AddNode(nodes[uid2]);
                    }
                    catch (Graph.NodeAlreadyExists e) { }
                    if (!edges.ContainsKey(key))
                    {
                        edges.Add(key, 1);
                        viewer.AddEdge(nodes[uid1], nodes[uid2]);
                    }
                    else if (edges[key] == 0)
                    {
                        edges[key] = edges[key] + 1;
                        viewer.AddEdge(nodes[uid1], nodes[uid2]);
                    }
                    else
                    {
                        edges[key]++;
                    }
                }
            }
		}

        public void addInitial()
        {
            currentPointer = 0;
            for (int i = 0; i < numToDisplay; i++)
            {
                add(photoList.ElementAt(i));
            }
        }

        public void next()
        {
			if(currentPointer+numToDisplay>=photoList.Count()){
			 return;
			}
			else{
              Photo removePhoto = photoList.ElementAt(currentPointer);
              remove(removePhoto);
              Photo addPhoto = photoList.ElementAt(currentPointer + numToDisplay);
              add(addPhoto);
              currentPointer++;
			}
		}
        public void previous()
        {
			if(currentPointer<=0){
			 return;
			}		
			else{
				currentPointer--;
				Photo addPhoto = photoList.ElementAt(currentPointer);
				Photo removePhoto = photoList.ElementAt(currentPointer+numToDisplay+1);
				remove(removePhoto);
				add(addPhoto);
			}
    	}
	}
}