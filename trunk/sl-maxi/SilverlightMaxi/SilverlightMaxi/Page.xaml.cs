using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace SilverlightMaxi
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

                Graph g = null;

                /* This is the file which will be rendered.
                 *  If you want to render a different file, you need to add it to your project
                 *  and, in the property panel for the file, set Build Action to "Embedded Resource"
                 */
                string graphResource = "SilverlightMaxi.friends.xml";
                System.IO.Stream s = this.GetType().Assembly.GetManifestResourceStream(graphResource);

                g = GraphReader.BuildGraph(s, true);

                /* Execute the spiffy graph visualizer */
                _v = new GraphViewer(g, new Size(800, 600));
                _v.DoLayout();

                /* Add the result to the current screen */
                this.FriendCluster.Children.Insert(0, _v.Canvas);

                _timer = new DispatcherTimer()
                {
                    Interval = new TimeSpan(0, 0, 0, 0, 45)
                };

                _timer.Tick += new EventHandler(_timer_Tick);

                _timer.Start();

        }

        void _timer_Tick(object sender, EventArgs e)
        {
            _v.StepLayout();
        }

        private void emailSearchButton_Click(object sender, RoutedEventArgs e)
        {
            runSearch();
        }

        private void emailSearchTextBox_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key.Equals(Key.Enter))
                runSearch();
        }

        private void runSearch()
        {
            // Check if email adress is in system
            // If so, generate graphs
            // Else, generate a message on how to get added to the system
        }
    }
}
