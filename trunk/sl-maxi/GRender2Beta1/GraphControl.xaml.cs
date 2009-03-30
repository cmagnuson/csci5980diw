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
using System.Collections.Generic;
using System.Windows.Controls;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace GRender
{
    public partial class Page : UserControl
    {
        private GraphViewer _v;
        private DispatcherTimer _timer;

        public Page()
        {
            InitializeComponent();
        }


        public void Page_Loaded(object o, EventArgs e)
        {
            // Required to initialize variables
            InitializeComponent();

            try
            {
                Graph g = null;

                /* This is the file which will be rendered.
                 *  If you want to render a different file, you need to add it to your project
                 *  and, in the property panel for the file, set Build Action to "Embedded Resource"
                 */
                string graphResource = "GRender.states.txt";

                try
                {
                    g = GraphReader.BuildGraph(this.GetType().Assembly.GetManifestResourceStream(graphResource));
                }
                catch
                {
                    this.Details.Text = "Error while loading graph: " + graphResource + ". \n Loading a random graph instead.";
                    g = GraphReader.BuildGraph(20); ;
                }

                /* Execute the spiffy graph visualizer */
                _v = new GraphViewer(g, new Size(800, 600));
                _v.DoLayout();

                /* Add the result to the current screen */
                this.MainCanvas.Children.Insert(0, _v.Canvas);

                _timer = new DispatcherTimer()
                {
                    Interval = new TimeSpan(0, 0, 0, 0, 45)
                };

                _timer.Tick += new EventHandler(_timer_Tick);

                _timer.Start();

            }
            catch (Exception ex)
            {
                this.Details.Text += ex.ToString();
            }
        }

        void _timer_Tick(object sender, EventArgs e)
        {
            _v.StepLayout();
        }

    }
}
