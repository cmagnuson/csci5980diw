using System;
using System.Windows.Input;
using System.Windows.Threading;
using System.IO;
using System.Linq;
using System.Xml;
using System.Xml.Linq;
using System.Collections.Generic;

namespace Graphite.Demo
{
    public partial class BlackPage
    {
        private Node previous;
        public BlackPage()
        {
            InitializeComponent();
            graphite.NodeHovering += graphite_ShowInfo;
            graphite.NodeClick += graphite_NodeClick;
            graphite.MouseLeftButtonDown += graphite_MouseLeftButtonDown;
            graphite.Loaded += graphite_Loaded;
            
        }

        void graphite_Loaded(object sender, System.Windows.RoutedEventArgs e)
        {
            buildGraph();       
        }




        void buildGraph()
        {
            /* This is the file which will be rendered.
 *  If you want to render a different file, you need to add it to your project
 *  and, in the property panel for the file, set Build Action to "Embedded Resource"
 */

            graphite.StopLayout();

            string graphResource = "Graphite.Demo.friends.xml";
            System.IO.Stream s = this.GetType().Assembly.GetManifestResourceStream(graphResource);


            XDocument xml = XDocument.Load(XmlReader.Create(s));
            XElement elementRoot = xml.Root; // Not sure if this is necessary
            var elements = from element in elementRoot.Descendants("friend")
                           select new
                           {
                               uid = (long)element.Element("uid"),
                               name = (string)element.Element("name"),
                               friendlist = from friend in element.Element("friendlist").Descendants()
                                            select (long)friend,
                           };

            Dictionary<long, Node> nodeDict = new Dictionary<long, Node>();
            foreach (var el in elements)
            {
                Node n = new Node();
                n.Title = el.name;
                n.Type = NodeType.Bubble;
                n.ID = ""+el.uid;
                graphite.AddNode(n);
                nodeDict.Add(el.uid, n);
            }
            foreach (var el in elements)
            {
                foreach (long fid in el.friendlist)
                {
                    Edge edge = new Edge();
                    edge.From = nodeDict[el.uid];
                    edge.To = nodeDict[fid];
                    if (fid == 13906770 || el.uid == 13906770)
                    {
                        continue;
                    }
                    if (graphite.HasEdge(edge.To, edge.From) || graphite.HasEdge(edge.From, edge.To))
                    {
                        continue;
                    }
                    graphite.AddEdge(edge.From, edge.To);
                }
            }

            graphite.UpdateLayout();
            graphite.StartLayout();
        }


        void graphite_ShowInfo(object sender, NodeEventArgs e)
        {
            info.Text = e.Node.Info.ToString();
        }
        void graphite_NodeClick(object sender, NodeEventArgs e)
        {
           
            if (Keyboard.Modifiers == ModifierKeys.Control)
            {
                graphite.AttachRandomNodeTo(e.Node);
                e.Handled = true;
                return;
            }
            if (Keyboard.Modifiers == ModifierKeys.Alt)
            {
                graphite.DeleteNode(e.Node);
                return;
            }
        }

        void graphite_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            if (e.Handled) return;
            if (Keyboard.Modifiers == ModifierKeys.Control)
            {
                graphite.AttachRandomNodeTo(null);
            }
        }
    }
}
