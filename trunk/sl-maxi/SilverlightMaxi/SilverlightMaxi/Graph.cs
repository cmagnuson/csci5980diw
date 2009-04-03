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
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Collections.Generic;

namespace SilverlightMaxi
{
    public class Node
    {
        public enum NodeType { Fact, Projection }

        public NodeType Type;
        public string Title;
        public long uid;
    }

    public class Graph
    {
        public class GraphNodes : IEnumerable<Node>
        {
            private Graph _parentGraph;
            private Dictionary<Node, bool> _nodeSet;

            public GraphNodes(Graph parentGraph)
            {
                _parentGraph = parentGraph;
                _nodeSet = new Dictionary<Node, bool>();
            }

            public bool Contains(Node node)
            {
                return _nodeSet.ContainsKey(node);
            }

            public IList<Node> Children(Node ofNode)
            {
                return _parentGraph.GetEdges(ofNode, _parentGraph._children);
            }

            public IList<Node> Parents(Node ofNode)
            {
                return _parentGraph.GetEdges(ofNode, _parentGraph._parents);
            }

            public void Add(Node node)
            {
                if (Contains(node))
                    throw new Exception("The graph already contains the node specified for addition.");
                else
                    _nodeSet[node] = true;
            }

            public void Remove(Node node)
            {
                if (Parents(node).Count > 0 || Children(node).Count > 0)
                    throw new Exception("The node cannot be removed because it still has connected edges.");

                if (!_nodeSet.Remove(node))
                    throw new Exception("The graph does not contain the node specified for removal.");
            }

            #region IEnumerable<Node> Members

            public IEnumerator<Node> GetEnumerator()
            {
                return _nodeSet.Keys.GetEnumerator();
            }

            #endregion

            #region IEnumerable Members

            System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
            {
                return GetEnumerator();
            }

            #endregion
        }

        public class GraphEdges
        {
            private Graph _parentGraph;

            public GraphEdges(Graph parentGraph)
            {
                _parentGraph = parentGraph;
            }

            public bool this[Node parent, Node child]
            {
                set { _parentGraph.SetEdge(parent, child, value); }
            }
        }

        protected Dictionary<Node, List<Node>> _children = new Dictionary<Node, List<Node>>();
        protected Dictionary<Node, List<Node>> _parents = new Dictionary<Node, List<Node>>();

        protected IList<Node> GetEdges(Node a, Dictionary<Node, List<Node>> adjacent)
        {
            List<Node> adj = null;
            if (adjacent.TryGetValue(a, out adj))
                return adj.AsReadOnly();
            else
                return new List<Node>(0).AsReadOnly();
        }

        protected void SetEdge(Node parent, Node child, bool value)
        {
            if (!Nodes.Contains(parent) || !Nodes.Contains(child))
                throw new Exception("One or both of the nodes attached to the edge is not contained in the graph.");

            List<Node> children = SetDirectedEdge(parent, child, _children, value);
            List<Node> parents  = SetDirectedEdge(child, parent, _parents, value);
        }

        /// <summary>
        /// Sets the status of the edge between note A and B and returns the updated adjacency list for node A.
        /// </summary>
        private List<Node> SetDirectedEdge(Node a, Node b, Dictionary<Node, List<Node>> adjacent, bool value)
        {
            List<Node> adj = null;

            if (adjacent.TryGetValue(a, out adj))
            {
                if (value == false)
                {
                    if (!adj.Remove(b))
                        throw new Exception("Edge specified for removal didn't exist.");

                    if (adj.Count == 0) // If this is the last item in the list ...
                    {
                        adjacent.Remove(a); // Remove the list itself
                        return adj;
                    }
                }
                else
                {
                    if (adj.Contains(b))
                        throw new Exception("Edge specified for addition already existed.");
                    else
                        adj.Add(b);
                }
            }
            else
            {
                if (value == false)
                {
                    throw new Exception("Edge specified for removal didn't exist.");
                }
                else // Need an edge where adj list doesn't exist -- so create both the list & edge
                {
                    adj = new List<Node>(new Node[] { b });
                    adjacent[a] = adj;
                }
            }

            return adj;
        }

        public GraphEdges Edges;
        public GraphNodes Nodes;

        public Graph()
        {
            Edges = new GraphEdges(this);
            Nodes = new GraphNodes(this);
        }

    }
}
