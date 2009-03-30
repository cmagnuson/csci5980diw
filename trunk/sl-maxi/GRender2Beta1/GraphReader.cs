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
using System.Collections.Generic;

namespace GRender
{
    public class GraphReader
    {
        /// <summary>
        /// Creates a graph by interpreting a specially formatted text file.
        /// </summary>
        /// <param name="path">The file to open for reading.</param>
        /// <returns>The graph created.</returns>
        public static Graph BuildGraph( System.IO.Stream s)
        {
            string[] graphLines = Sys.IO.File.ReadAllLines( s );
            return BuildGraph(graphLines);
        }

        /// <summary>
        /// Builds a graph represented by the string array encoding.
        /// </summary>
        /// <param name="serverNodes">String array from the graph API.</param>
        /// <returns>The graph as represented by local the graph library.</returns>
        public static Graph BuildGraph(string[] serverNodes)
        {
            /* Instantiate our lightweight graph library */
            Graph g = new Graph();

            Dictionary<long, Node> nodeDict = new Dictionary<long, Node>();

            // First pass, get all the nodes
            foreach (string node in serverNodes)
            {
                string[] nodeData = node.Split('/');

                if (nodeData.Length < 2) continue;

                Node n = new Node()
                {
                    Title = nodeData[1]
                };

                if (nodeData.Length <= 4)
                {
                    n.Type = Node.NodeType.Fact;
                }
                else if (nodeData[4] == "Fact")
                {
                    n.Type = Node.NodeType.Fact;
                }
                else
                {
                    n.Type = Node.NodeType.Projection;
                }

                nodeDict.Add(long.Parse(nodeData[0]), n);

                g.Nodes.Add(n);
            }

            // Second pass, get the edges
            foreach (string node in serverNodes)
            {
                string[] nodeData = node.Split('/');

                long nodeId = long.Parse(nodeData[0]);

                if (nodeData.Length > 2)
                {
                    string[] nodeChildren = nodeData[2].Split(',');
                    foreach (string child in nodeChildren)
                    {
                        if (child.Length == 0) continue;
                        g.Edges[nodeDict[nodeId], nodeDict[long.Parse(child)]] = true;
                    }
                }
            }

            return g;
        }

        /// <summary>
        /// For testing purposes: Builds a random graph with N nodes.
        /// </summary>
        /// <param name="n">The number of nodes and random edges.</param>
        /// <returns>The random graph as represented by the local graph library.</returns>
        public static Graph BuildGraph(int N)
        {
            Graph g = new Graph();

            List<Node> allNodes = new List<Node>();

            /* Create N nodes */
            for (int i = 0; i < N; i++)
            {
                Node n = new Node() { Type = Node.NodeType.Fact, Title = i.ToString() };
                allNodes.Add(n);
                g.Nodes.Add(n);
            }

            // Add N Random Edges ------
            Random r = new Random();
            for (int i = 0; i < N; i++)
            {
                int dest = r.Next(allNodes.Count);
                if (dest == i) continue;
                if (!g.Nodes.Children(allNodes[i]).Contains(allNodes[dest]))
                    g.Edges[allNodes[i], allNodes[dest]] = true;
            }

            return g;
        }




    }
}
