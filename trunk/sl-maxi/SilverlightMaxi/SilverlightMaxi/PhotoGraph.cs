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
		Dictionary<String, Int> edges = new Dictionary<String, Int>();
		Dictionary<long, Node> nodes = new Dictionary<long, Node>();
		
		public PhotoGraph(List<Photo> pl, int toDisplay, Dictionary<long, Node> ns){
			photoList = pl;
			numToDisplay = toDisplay;
			nodes = ns;
		}
		
		//decrement edge values in dictionary by 1, if 0 then remove from graph
		public void remove(Photo photo){
			for(int i=photo.getTaggedList().size(); i++){
				for(int j=i+1; j<photo.getTaggedList(); j++){
					long uid1 = photo.getTaggedList().get(i);
					long uid2 = photo.getTaggedList.get(j);
					String key = uid1+","+uid2;
					if(!edges.contains(key)){
						edges.put(key, 1);
					}
					else if(edges.get(key)==1){
						g.setEdge(nodes.get(uid1), nodes.get(uid2), false);
					}
					else if(edges.get(key)<1){
						//should never happen!
						edges.put(key, 1);
					}
					edges.put(key, edges.get(key)-1);
				}
			}
		}
		//increment edge values in dictionary by 1, if starting at 0 or not existing then add to graph
		public void add(Photo photo){
			for(int i=photo.getTaggedList().size(); i++){
				for(int j=i+1; j<photo.getTaggedList(); j++){
					long uid1 = photo.getTaggedList().get(i);
					long uid2 = photo.getTaggedList.get(j);
					String key = uid1+","+uid2;
					if(!edges.contains(key)){
						edges.put(key, 0);
						g.setEdge(nodes.get(uid1), nodes.get(uid2), true);
					}
					else if(edges.get(key)==0){
						g.setEdge(nodes.get(uid1), nodes.get(uid2), true);
					}
					edges.put(key, edges.get(key)+1);
				}
			}
		}
		
		public void next(Graph g){
			if(currentPointer+numToDisplay>=photoList.size()){
			 return;
			}
			else{
			  currentPointer++;
			  	Photo addPhoto = photoList.get(currentPointer+numToDisplay);
				Photo removePhoto = photoList.get(currentPointer-1);
				remove(removePhoto);
				add(addPhoto);
			}
		}
		public void previous(Graph g){
			if(currentPointer<=0){
			 return;
			}		
			else{
				currentPointer--;
				Photo addPhoto = photoList.get(currentPointer);
				Photo removePhoto = photoList.get(currentPointer+numToDisplay+1);
				remove(removePhoto);
				add(addPhoto);
			}
    	}
	}
}