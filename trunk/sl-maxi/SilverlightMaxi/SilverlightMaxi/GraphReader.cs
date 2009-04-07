/*  This file is part of GRender.
 *
 *  Send feedback and check for updates on on http://fluxcapacity.net
 *  
 *  GRender is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GRender is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


using System;
using System.IO;
using System.Linq;
using System.Xml;
using System.Xml.Linq;
using System.Collections.Generic;

namespace SilverlightMaxi
{
    public class GraphReader
    {
    
        public static Dictionary<long, Node> nodeDict;
    	

        /// <summary>
        /// Creates a graph by interpreting a specially formatted text file.
        /// </summary>
        /// <param name="path">The file to open for reading.</param>
        /// <returns>The graph created.</returns>
        public static Graph BuildGraph( System.IO.Stream s, bool addEdges)
        {
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
            Graph g = new Graph();
            nodeDict = new Dictionary<long, Node>();
            Node n = null;
            foreach (var el in elements)
            {
                n = new Node() { Title = el.name, Type = Node.NodeType.Fact, uid = el.uid };
                nodeDict.Add(n.uid, n);
                g.Nodes.Add(n);
            }
            
            if(addEdges){
            foreach (var el in elements)
            {
                foreach (long fid in el.friendlist)
                {
                    if (nodeDict.ContainsKey(fid))
                    {
                        g.Edges[nodeDict[el.uid], nodeDict[fid]] = true;
                    }
                }
            }
            }
            return g;
        }

    }
}
