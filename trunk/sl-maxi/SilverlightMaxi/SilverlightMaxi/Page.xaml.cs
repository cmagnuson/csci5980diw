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
        private GraphViewer _v, _v2;
        private DispatcherTimer _timer;
        private PhotoGraph pg;
        Graph g2;

        public Page()
        {
            // Required to initialize variables
            InitializeComponent();
        }

        void _timer_Tick(object sender, EventArgs e)
        {
            _v.StepLayout(15);
            _v2.StepLayout(15);
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
            List<string> emailAddresses = new List<string>();
            emailAddresses.Add("magnu213@umn.edu");
            //emailAddresses.Add("ande7966@umn.edu");
            emailAddresses.Add("norg0013@umn.edu");

            string searchTerm = this.emailSearchTextBox.Text.Trim().ToLower();

            // If so, generate graphs
            if ( emailAddresses.Contains( searchTerm ) )
            {
                Graph g = null;

                /* This is the file which will be rendered.
                 *  If you want to render a different file, you need to add it to your project
                 *  and, in the property panel for the file, set Build Action to "Embedded Resource"
                 */
                string graphResource = "SilverlightMaxi." + searchTerm + ".friends.xml";
                string photoResource = "SilverlightMaxi." + searchTerm + ".photos.xml";
                System.IO.Stream s = this.GetType().Assembly.GetManifestResourceStream(graphResource);

                g = GraphReader.BuildGraph(s, true);
                _v = new GraphViewer(g, new Size(800, 600));
                _v.DoLayout();

                s = this.GetType().Assembly.GetManifestResourceStream(graphResource);
                g2 = GraphReader.BuildGraph(s, false);
                _v2 = new GraphViewer(g2, new Size(800, 600));
                _v2.DoLayout();

                pg = new PhotoGraph
                {
                    photoList = Photo.getPhotoList(photoResource, this),
                    nodes = GraphReader.nodeDict,
                    numToDisplay = 50,
                    viewer = _v2,
                };

                pg.addInitial();
                _v2.StepLayout(50);
              

                /* Add the result to the current screen */
                this.FriendCluster.Children.Clear();
                this.FriendCluster.Children.Insert(0, _v.Canvas);

                this.FriendPhotos.Children.Clear();
                this.FriendPhotos.Children.Insert(0, _v2.Canvas);
                Button b = new Button();
                b.Click += new RoutedEventHandler(nextPhoto_Click);
                b.Content = "Next 50 Photos";
                b.Width = 100;
                b.Height = 20;
                this.FriendPhotos.Children.Insert(1, b);

                _timer = new DispatcherTimer()
                {
                    Interval = new TimeSpan(0, 0, 0, 0, 45)
                };

                _timer.Tick += new EventHandler(_timer_Tick);

                _timer.Start();
            }
            // Else, generate a message on how to get added to the system
            else
            {
                string message = "Your email address, " + searchTerm + ", was not found in the system. To add it...";

                TextBlock emailNotFoundMessage1 = new TextBlock();
                emailNotFoundMessage1.Text = message;

                TextBlock emailNotFoundMessage2 = new TextBlock();
                emailNotFoundMessage2.Text = message;

                this.FriendCluster.Children.Clear();
                this.FriendCluster.Children.Add(emailNotFoundMessage1);

                this.FriendPhotos.Children.Clear();
                this.FriendPhotos.Children.Add(emailNotFoundMessage2);
            }
        }

        private void nextPhoto_Click(object sender, RoutedEventArgs e)
        {
            for (int i = 0; i < 50; i++)
            {
                pg.next();
            }
            _v2.StepLayout(1);
            //emailSearchTextBox.Text = pg.currentPointer.ToString();
        }
    }
}
