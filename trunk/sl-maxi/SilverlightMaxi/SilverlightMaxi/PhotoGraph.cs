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
using System.Windows.Threading;
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
		public int numToDisplay = 50;
		private int currentPointer = 0;
        private int endPointer;
		Dictionary<String, int> edges = new Dictionary<String, int>();
		public Dictionary<long, Node> nodes = new Dictionary<long, Node>();
        public GraphViewer viewer;
        private DispatcherTimer playTimer = new DispatcherTimer(){
            Interval = new TimeSpan(0, 0, 0, 0, 90)};
        public Slider position;
		
		
		//decrement edge values in dictionary by 1, if 0 then remove from graph
		private void remove(Photo photo){
            for (int i = 0;  i<photo.getTaggedList().Count(); i++)
            {
                for (int j = i + 1; j < photo.getTaggedList().Count(); j++)
                {
                    long uid1 = photo.getTaggedList().ElementAt(i);
                    long uid2 = photo.getTaggedList().ElementAt(j);
                    if (uid1 > uid2) {
                        long tmp = uid1;
                        uid1 = uid2;
                        uid2 = tmp;
                    }
                    String key = uid1 + "," + uid2;
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
                    if (edges[key] > 0)
                        viewer.setEdgeWidth(nodes[uid1], nodes[uid2], ((double)edges[key]) / 2);
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
                    if (uid1 > uid2) {
                        long tmp = uid1;
                        uid1 = uid2;
                        uid2 = tmp;
                    }
                    String key = uid1 + "," + uid2;
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
                        viewer.setEdgeWidth(nodes[uid1], nodes[uid2], ((double)edges[key]) / 2);
                    }
                }
            }
		}

        public void addInitial()
        {
            for (int p = 0; p < photoList.Count(); p++)
            {
                Photo photo = photoList.ElementAt(p);
                for (int i = 0; i < photo.getTaggedList().Count(); i++)
                {
                    long uid = photo.getTaggedList().ElementAt(i);
                    if (!nodes.ContainsKey(uid))
                        nodes.Add(uid, new Node() { Title = "You", uid = uid });
                    try
                    {
                        viewer.AddNode(nodes[uid]);
                    }
                    catch (Graph.NodeAlreadyExists) { }
                }
            }

            playTimer.Tick +=new EventHandler(playTimer_Tick);
            currentPointer = 0;
            for (int i = 0; i < numToDisplay; i++)
            {
                add(photoList.ElementAt(i));
            }
        }

        public void playForward()
        {
            endPointer = photoList.Count() - numToDisplay;
            playTimer.Start();
        }

        public void playBackward()
        {
            endPointer = 0;
            playTimer.Start();
        }

        public void stop()
        {
            playTimer.Stop();
        }

        void playTimer_Tick(object sender, EventArgs e)
        {
 	        if (currentPointer < endPointer)
                next();
            if (currentPointer > endPointer)
                previous();
            if (currentPointer == endPointer)
                playTimer.Stop();
            viewer.StepLayout(1);
        }

        public void next()
        {
            if (currentPointer + numToDisplay >= photoList.Count()) throw new Exception("can't go forward");
            else
            {
                Photo removePhoto = photoList.ElementAt(currentPointer);
                remove(removePhoto);
                Photo addPhoto = photoList.ElementAt(currentPointer + numToDisplay);
                add(addPhoto);
                currentPointer++;
            }
            position.Value = currentPointer;
		}
        public void previous()
        {
            if (currentPointer <= 0) throw new Exception("Can't go back");
            else
            {
                currentPointer--;
                Photo addPhoto = photoList.ElementAt(currentPointer);
                Photo removePhoto = photoList.ElementAt(currentPointer + numToDisplay);
                remove(removePhoto);
                add(addPhoto);
            }
            position.Value = currentPointer;
    	}

        public int getCurrentPointer()
        {
            return currentPointer;
        }

        public void setNumToDisplay(int num_display)
        {
            while (numToDisplay > num_display)
            {
                numToDisplay--;
                remove(photoList.ElementAt(currentPointer + numToDisplay));
                if (endPointer > currentPointer) endPointer++;
            }
            while (numToDisplay < num_display && currentPointer + numToDisplay < photoList.Count())
            {
                add(photoList.ElementAt(currentPointer + numToDisplay));
                numToDisplay++;
                if (endPointer > currentPointer) endPointer--;
            }
            while (numToDisplay < num_display) {
                currentPointer--;
                if (currentPointer < 0) throw new Exception("should not happen");
                add(photoList.ElementAt(currentPointer));
                numToDisplay++;
                if (endPointer > currentPointer) endPointer--;
            }
            position.Maximum = photoList.Count() - numToDisplay;
            position.Value = currentPointer;
        }
	}
}