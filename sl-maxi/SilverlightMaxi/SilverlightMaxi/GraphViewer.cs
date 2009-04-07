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
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace SilverlightMaxi
{
    /// <summary>
    /// A physics-based Graph rendering utility for Silverlight.
    /// 
    /// Jeff Powers, Aug 2007
    /// fluxcapacity
    /// http://fluxcapacity.net
    /// 
    /// The layout algorithm outline can be found here:
    /// http://en.wikipedia.org/wiki/Force-based_algorithms
    /// </summary>
    public class GraphViewer
    {
        private class NodeCanvas : Canvas
        {
            public NodeState NodeState;
        }

        /// <summary>
        /// Helper class to store visual and physical description for the nodes.
        /// </summary>
        private class NodeState
        {
            public Point Position;
            public Point Velocity;
            public NodeCanvas NodeCanvas;
            public Dictionary<Node, Line> ChildLinks
                 = new Dictionary<Node, Line>();
        }

        private Point _center;
        private Graph _graph;
        private Canvas _canvas;
        private Dictionary<Node, NodeState> _nodeState;
        private bool isPurpleSkin;

        public Canvas Canvas { get { return _canvas; } }

        private Random r;

        public GraphViewer(Graph g, Size size, bool setSkin)
        {
            isPurpleSkin = setSkin;

            _graph = g;
            _canvas = new Canvas();
            _canvas.Width = size.Width;
            _canvas.Height = size.Height;
            _canvas.Background = new SolidColorBrush(Color.FromArgb(255, 0, 0, 0));
            _nodeState = new Dictionary<Node, NodeState>();

            Rectangle border = new Rectangle()
            {
                Width = _canvas.Width,
                Height = _canvas.Height,
                Stroke = new SolidColorBrush(Color.FromArgb(255,128, 128, 128))
            };

            _canvas.Children.Add(border);
        }
        
        public void DoLayout( )
        {
            _center = new Point(_canvas.Width / 2, _canvas.Height / 2);

            r = new Random();

            foreach (Node n in _graph.Nodes)
            {
                AddNodeToCanvas(n);
            }

            #region Build visual graph representation

            foreach (KeyValuePair<Node, NodeState> nodeLoc in _nodeState)
            {
                foreach (Node child in _graph.Nodes.Children(nodeLoc.Key))
                {
                    AddEdgeToCanvas(nodeLoc.Key, child);
                }
            }
            StepLayout(50);
            #endregion
        
        }

        public void AddNode(Node n)
        {
            _graph.Nodes.Add(n);
            AddNodeToCanvas(n);
        }

        public void RemoveNode(Node n)
        {
            if (_nodeState[n].ChildLinks.Count > 0) throw new Exception("Can't remove until all edges are gone.");
            _graph.Nodes.Remove(n);
            _canvas.Children.Remove(_nodeState[n].NodeCanvas);
            _nodeState.Remove(n);
        }

        protected void AddNodeToCanvas(Node n)
        {
            Point randomPos = new Point(300 + 200 * (r.NextDouble() - 0.5), 300 + 150 * (r.NextDouble() - 0.5));
            NodeState ns = new NodeState()
                {
                    Position = randomPos,
                    Velocity = new Point(0, 0),
                    NodeCanvas = CreateNodeCanvas(n),
                };
            ns.NodeCanvas.NodeState = ns;
            _nodeState.Add(n, ns);

            _canvas.Children.Add(ns.NodeCanvas);
        }

        public void AddEdge(Node n1, Node n2)
        {
            _graph.Edges[n1, n2] = true;
            _graph.Edges[n2, n1] = true;
            AddEdgeToCanvas(n1, n2);
        }

        protected void AddEdgeToCanvas(Node n1, Node n2) {
            Line edge = new Line()
            {
                Stroke = new SolidColorBrush { Color = Color.FromArgb(255, 128, 128, 255) },
                StrokeThickness = 0.5
            };

            if (_nodeState[n1].ChildLinks.ContainsKey(n2) && _nodeState[n2].ChildLinks.ContainsKey(n1))
            {
                return;
            }
            _nodeState[n1].ChildLinks[n2] = edge;
            _nodeState[n2].ChildLinks[n1] = edge;

            _canvas.Children.Insert(0, edge);
        }

        public void setEdgeWidth(Node n1, Node n2, Double width)
        {
            _nodeState[n1].ChildLinks[n2].StrokeThickness = width;
        }

        public void RemoveEdge(Node n1, Node n2)
        {
            _graph.Edges[n1, n2] = false;
            _graph.Edges[n2, n1] = false;
            Line edge = _nodeState[n1].ChildLinks[n2];
            _canvas.Children.Remove(edge);
            _nodeState[n1].ChildLinks.Remove(n2);
            _nodeState[n2].ChildLinks.Remove(n1);
            /*
            if (_nodeState[n1].ChildLinks.Count == 0)
            {
                RemoveNode(n1);
            }

            if (_nodeState[n2].ChildLinks.Count == 0)
            {
                RemoveNode(n2);
            }
             */
        }

        /// <summary>
        /// Applies one step of the graph layout algorithm, moving nodes to a more stable configuration.
        /// </summary>
        public void StepLayout(int steps)
        {
            for (int i = 0; i < steps; i++)
            {
                double KE = RunPhysics();

                /* This causes the centering effect, but we disable it
                 * temporarily while dragging so that it doesn't move the
                 * item beneath the user's cursor */

                if (!DragState.IsDragging)
                    ApplyGlobalOffset(_center, 0.5);
            }
            UpdateVisualPositions();
        }

        private void UpdateVisualPositions()
        {
            foreach (KeyValuePair<Node, NodeState> nodeLoc in _nodeState)
            {
                NodeState ns = nodeLoc.Value;

                /* Set the position of the node */
                ns.NodeCanvas.SetValue(Canvas.LeftProperty, ns.Position.X - ns.NodeCanvas.Width / 2);
                ns.NodeCanvas.SetValue(Canvas.TopProperty, ns.Position.Y - ns.NodeCanvas.Height / 2);

                foreach (KeyValuePair<Node, Line> kvp in ns.ChildLinks)
                {
                    Point childLoc = _nodeState[kvp.Key].Position;
                    kvp.Value.X1 = ns.Position.X;
                    kvp.Value.Y1 = ns.Position.Y;
                    kvp.Value.X2 = childLoc.X;
                    kvp.Value.Y2 = childLoc.Y;
                }
            }
        }

        private void ApplyGlobalOffset(Point DesiredCentroid, double PixelSpeed)
        {
            Point centroid = new Point();

            foreach (NodeState state in _nodeState.Values)
            {
                centroid.X += state.Position.X;
                centroid.Y += state.Position.Y;
                //centroid.Offset(state.Position.X, state.Position.Y);
            }

            centroid.X = centroid.X / _nodeState.Count;
            centroid.Y = centroid.Y / _nodeState.Count;

            Point offset = new Point(
                DesiredCentroid.X - centroid.X, 
                DesiredCentroid.Y - centroid.Y);

            /* Normalize the offset and only move by the desired pixel speed */
            double length = Math.Sqrt(offset.X * offset.X + offset.Y * offset.Y);

            /* If we're very close, don't move at the full speed anymore */
            if (PixelSpeed > length) PixelSpeed = length;

            offset.X = (offset.X / length) * PixelSpeed;
            offset.Y = (offset.Y / length) * PixelSpeed;

            foreach (NodeState state in _nodeState.Values)
            {
                //state.Position.Offset(offset.X, offset.Y);
                state.Position.X += offset.X;
                state.Position.Y += offset.Y;
            }
        }

        #region Physics Simulation Components

        /// <summary>
        /// Simulates 1/r^2 repulsive force as with two similarly-charged electrons.
        /// Also adds a very weak attractive force proportional to r which keeps
        /// disparate graph segments from flying apart.
        /// </summary>
        private Point CoulombRepulsion(Point a, Point b, double k)
        {
            double dx = a.X - b.X, dy = a.Y - b.Y;
            double sqDist = dx * dx + dy * dy;
            double d = Math.Sqrt(sqDist);

            double mag = k * 1.0 / sqDist; // Force magnitude

            mag += -k * 0.00000006 * d; // plus WEAK attraction

            if (mag > 10) mag = 10; // Clip maximum

            return new Point( mag * (dx / d) , mag * (dy / d));
        }

        private Point HookeAttraction(Point a, Point b, double k)
        {
            double dx = a.X - b.X, dy = a.Y - b.Y;
            double sqDist = dx * dx + dy * dy;
            double d = Math.Sqrt(sqDist);

            double mag = -k * 0.001 * Math.Pow( d, 1.0); // Force magnitude

            return new Point(mag * (dx / d), mag * (dy / d));
        }

        /// <summary>
        /// Applies simulated electrostatic repulsion between nodes and spring
        /// attraction along edges, moving nodes to a more stable configuration.
        /// </summary>
        /// <returns></returns>
        private double RunPhysics()
        {
            double totalKE = 0;

            foreach (KeyValuePair<Node, NodeState> kvp in _nodeState)
            {
                if (DragState.IsDragging && DragState.NodeBeingDragged == kvp.Value) continue;

                double dT = 0.95;

                Node n = kvp.Key;
                NodeState state = kvp.Value;

                Point F = new Point(0, 0); // Force

                /* for each other node ... */
                foreach (KeyValuePair<Node, NodeState> kvpB in _nodeState)
                {
                    if(kvpB.Key == n) continue;
                    Point coulomb = CoulombRepulsion(state.Position, kvpB.Value.Position, 300.0);
                    F.X += coulomb.X;
                    F.Y += coulomb.Y;
                    //F.Offset(coulomb.X, coulomb.Y); 
                }

                /* foreach spring connected ... */
                foreach (Node child in _graph.Nodes.Children(n))
                {
                    Point hooke = HookeAttraction(state.Position, _nodeState[child].Position, 0.9);
                    F.X += hooke.X;
                    F.Y += hooke.Y;
                    //F.Offset(hooke.X, hooke.Y);
                }
                foreach (Node parent in _graph.Nodes.Parents(n))
                {
                    Point hooke = HookeAttraction(state.Position, _nodeState[parent].Position, 0.9);
                    F.X += hooke.X;
                    F.Y += hooke.Y;
                    //F.Offset(hooke.X, hooke.Y);
                }

                Point v = state.Velocity;

                double damping = 0.90;

                /* Update velocity */
                state.Velocity = new Point(
                    (v.X + dT * F.X) * damping, 
                    (v.Y + dT * F.Y) * damping);

                totalKE += state.Velocity.X * state.Velocity.X + state.Velocity.Y * state.Velocity.Y;

                /* Update position */
                state.Position.X += dT * state.Velocity.X;
                state.Position.Y += dT * state.Velocity.Y;
                //state.Position.Offset(dT * state.Velocity.X, dT * state.Velocity.Y);
            }

            return totalKE;
        }

        #endregion

        #region Visual Appearance of one Node

        private NodeCanvas CreateNodeCanvas(Node n)
        {
            Brush nodeColor = new SolidColorBrush(Color.FromArgb(255,255, 100, 100));

            double Height = 10d;
            double Width = 10d;

            NodeCanvas nodeCanvas = new NodeCanvas() { Width = Width, Height = Height };
            
            ToolTip nameToolTip = new ToolTip();
            nameToolTip.Content = n.Title;
            ToolTipService.SetToolTip(nodeCanvas, nameToolTip);
            

            Rectangle nodeBkg = new Rectangle()
            {
                Width = Width,
                Height = Height,
                Fill = nodeColor,
                Stroke = nodeColor,
            };

            switch (n.Type)
            {
                case Node.NodeType.Fact:
                    nodeBkg.RadiusX = 10;
                    nodeBkg.RadiusY = 10;
                    break;
                case Node.NodeType.Projection:
                    nodeBkg.RadiusX = 2;
                    nodeBkg.RadiusY = 2;
                    break;
            }

            nodeCanvas.MouseLeftButtonDown += new MouseButtonEventHandler(nodeCanvas_MouseLeftButtonDown);
            Canvas.MouseMove += new MouseEventHandler(Canvas_MouseMove);
            Canvas.MouseLeftButtonUp += new MouseButtonEventHandler(Canvas_MouseLeftButtonUp);
            Canvas.MouseLeave += new MouseEventHandler(Canvas_MouseLeave);

            nodeCanvas.Children.Add(nodeBkg);

            return nodeCanvas;
        }

        void Canvas_MouseLeave(object sender, MouseEventArgs e)
        {
            DragState.IsDragging = false;
        }

        void Canvas_MouseLeftButtonUp(object sender, MouseButtonEventArgs e)
        {
            DragState.IsDragging = false;
        }

        void Canvas_MouseMove(object sender, MouseEventArgs e)
        {
            if(DragState.IsDragging)
            {
                Point position = e.GetPosition(DragState.NodeBeingDragged.NodeCanvas);
                position.X += (-DragState.OffsetWithinNode.X);
                position.Y += (-DragState.OffsetWithinNode.Y);
                //position.Offset(-DragState.OffsetWithinNode.X, -DragState.OffsetWithinNode.Y);

                NodeState ns = DragState.NodeBeingDragged;

                ns.Position.X += position.X; ns.Position.Y += position.Y;
                ns.NodeCanvas.SetValue(Canvas.LeftProperty, ns.Position.X - ns.NodeCanvas.Width / 2);
                ns.NodeCanvas.SetValue(Canvas.TopProperty, ns.Position.Y - ns.NodeCanvas.Height / 2);

                foreach (KeyValuePair<Node, Line> kvp in ns.ChildLinks)
                {
                    Point childLoc = _nodeState[kvp.Key].Position;
                    kvp.Value.X1 = ns.Position.X;
                    kvp.Value.Y1 = ns.Position.Y;
                    kvp.Value.X2 = childLoc.X;
                    kvp.Value.Y2 = childLoc.Y;
                }
            }
        }

        struct NodeDragState
        {
            public bool IsDragging;
            public NodeState NodeBeingDragged;
            public Point OffsetWithinNode;
        };

        private NodeDragState DragState;

        void nodeCanvas_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            NodeCanvas nodeCanvas = sender as NodeCanvas;

            DragState.NodeBeingDragged = nodeCanvas.NodeState;
            DragState.OffsetWithinNode = e.GetPosition(nodeCanvas);
            DragState.IsDragging = true;
        }

        #endregion

    }
}
