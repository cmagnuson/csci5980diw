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
        bool isPurpleSkin = true; // false if green skin
        Slider num_photos;

        public Page()
        {
            // Required to initialize variables
            InitializeComponent();
        }

        void _timer_Tick(object sender, EventArgs e)
        {
            if (FriendTab.IsSelected)
                _v.StepLayout(15);
            if (PhotoTab.IsSelected)
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
            emailAddresses.Add("ande7966@umn.edu");
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
                _v = new GraphViewer(g, new Size(800, 600), isPurpleSkin);
                _v.DoLayout();

                _v2 = new GraphViewer(new Graph(), new Size(800, 600), isPurpleSkin);
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
                pg.position = new Slider()
                {
                    Maximum = pg.photoList.Count() - pg.numToDisplay,
                    Value = pg.getCurrentPointer(),
                    MinWidth = 600,
                    IsEnabled = false,
                };
                this.FriendPhotos.Children.Add(pg.position);

                num_photos = new Slider()
                {
                    Minimum = 1,
                    Maximum = pg.photoList.Count(),
                    Value = 50,
                    MinWidth = 100,
                };
                num_photos.SetValue(Canvas.LeftProperty, 700.0);
                num_photos.ValueChanged += new RoutedPropertyChangedEventHandler<double>(num_photos_ValueChanged);
                this.FriendPhotos.Children.Add(num_photos);

                Button prevButton = new Button()
                {
                    Content = "Play Backward",
                    Width = 100,
                    Height = 20,
                };
                prevButton.SetValue(Canvas.TopProperty, 20.0);
                prevButton.Click += delegate { pg.playBackward(); };
                this.FriendPhotos.Children.Add(prevButton);

                Button stopButton = new Button()
                {
                    Content = "Stop",
                    Width = 50,
                    Height = 20,
                };
                stopButton.SetValue(Canvas.TopProperty, 20.0);
                stopButton.SetValue(Canvas.LeftProperty, 100.0);
                stopButton.Click += delegate { pg.stop(); };
                this.FriendPhotos.Children.Add(stopButton);

                Button nextButton = new Button()
                {
                    Content = "Play Forward",
                    Width = 100,
                    Height = 20,
                };
                nextButton.SetValue(Canvas.TopProperty, 20.0);
                nextButton.SetValue(Canvas.LeftProperty, 150.0);
                nextButton.Click += delegate { pg.playForward(); };
                this.FriendPhotos.Children.Add(nextButton);
                
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
                string message = "Your email address, " + searchTerm + ", was not found in the system.";

                TextBlock emailNotFoundMessage1 = new TextBlock();
                emailNotFoundMessage1.Foreground = new SolidColorBrush(Color.FromArgb(255, 255, 255, 255));
                emailNotFoundMessage1.Text = message;

                TextBlock emailNotFoundMessage2 = new TextBlock();
                emailNotFoundMessage2.Foreground = new SolidColorBrush(Color.FromArgb(255, 255, 255, 255));
                emailNotFoundMessage2.Text = message;

                this.FriendCluster.Children.Clear();
                this.FriendCluster.Children.Add(emailNotFoundMessage1);

                this.FriendPhotos.Children.Clear();
                this.FriendPhotos.Children.Add(emailNotFoundMessage2);
            }
        }

        void num_photos_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            DispatcherTimer timer = new DispatcherTimer();
            timer.Interval = new TimeSpan(0, 0, 0, 0, 500);
            timer.Tick += delegate
            {
                timer.Stop();
                pg.setNumToDisplay((int)num_photos.Value);
            };
            timer.Start();
        }

        private void skinButton_Click(object sender, RoutedEventArgs e)
        {
            if (isPurpleSkin)
                isPurpleSkin = false;
            else
                isPurpleSkin = true;

            if (isPurpleSkin)
            {
                this.skinButton.Content = "Show Green Skin";
                this.LayoutRoot.Background = new SolidColorBrush(Color.FromArgb(255, 74, 3, 111));
                this.title.Foreground = new SolidColorBrush(Color.FromArgb(255, 251, 254, 0));
                this.emailSearchText.Foreground = new SolidColorBrush(Color.FromArgb(255, 206, 0, 113));
                this.emailSearchButton.Foreground = new SolidColorBrush(Color.FromArgb(255, 33, 6, 114));
                this.emailSearchButton.Background = new SolidColorBrush(Color.FromArgb(255, 161, 61, 213));
                this.tabs.Background = new SolidColorBrush(Color.FromArgb(255, 175, 102, 213));
                this.FriendTab.Background = new SolidColorBrush(Color.FromArgb(255, 175, 102, 213));
                this.FriendTab.Foreground = new SolidColorBrush(Color.FromArgb(255, 33, 6, 114));
                this.PhotoTab.Background = new SolidColorBrush(Color.FromArgb(255, 175, 102, 213));
                this.PhotoTab.Foreground = new SolidColorBrush(Color.FromArgb(255, 33, 6, 114));
                this.FriendCluster.Background = new SolidColorBrush(Color.FromArgb(255, 206, 0, 113));
                this.FriendPhotos.Background = new SolidColorBrush(Color.FromArgb(255, 206, 0, 113));
                this.skinButton.Foreground = new SolidColorBrush(Color.FromArgb(255, 33, 6, 114));
                this.skinButton.Background = new SolidColorBrush(Color.FromArgb(255, 161, 61, 213));
                this.copyright.Foreground = new SolidColorBrush(Color.FromArgb(255, 206, 0, 113));
                if (this._v != null)
                {

                    this._v.setLineColor(new SolidColorBrush(Color.FromArgb(255, 74, 3, 111)));
                    this._v2.setLineColor(new SolidColorBrush(Color.FromArgb(255, 74, 3, 111)));
                    this._v.setNodeColor(new SolidColorBrush(Color.FromArgb(255, 251, 254, 0)));
                    this._v2.setNodeColor(new SolidColorBrush(Color.FromArgb(255, 251, 254, 0)));
                    this._v.setBackgroundColor(new SolidColorBrush(Color.FromArgb(255, 206, 0, 113)));
                    this._v2.setBackgroundColor(new SolidColorBrush(Color.FromArgb(255, 206, 0, 113)));
                }
            }
            else // isGreenSkin
            {
                this.skinButton.Content = "Show Purple Skin";
                this.LayoutRoot.Background = new SolidColorBrush(Color.FromArgb(255, 0, 182, 79));
                this.title.Foreground = new SolidColorBrush(Color.FromArgb(255, 166, 35, 0));
                this.emailSearchText.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.emailSearchButton.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.emailSearchButton.Background = new SolidColorBrush(Color.FromArgb(255, 181, 243, 109));
                this.tabs.Background = new SolidColorBrush(Color.FromArgb(255, 181, 243, 109));
                this.FriendTab.Background = new SolidColorBrush(Color.FromArgb(255, 181, 243, 109));
                this.FriendTab.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.PhotoTab.Background = new SolidColorBrush(Color.FromArgb(255, 181, 243, 109));
                this.PhotoTab.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.FriendCluster.Background = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.FriendPhotos.Background = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.skinButton.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                this.skinButton.Background = new SolidColorBrush(Color.FromArgb(255, 181, 243, 109));
                this.copyright.Foreground = new SolidColorBrush(Color.FromArgb(255, 3, 71, 105));
                if (this._v != null)
                {

                    this._v.setLineColor(new SolidColorBrush(Color.FromArgb(255, 128, 128, 255)));
                    this._v2.setLineColor(new SolidColorBrush(Color.FromArgb(255, 128, 128, 255)));
                    this._v.setNodeColor(new SolidColorBrush(Color.FromArgb(255, 128, 255, 128)));
                    this._v2.setNodeColor(new SolidColorBrush(Color.FromArgb(255, 128, 255, 128)));
                    this._v.setBackgroundColor(new SolidColorBrush(Color.FromArgb(255, 0, 0, 0)));
                    this._v2.setBackgroundColor(new SolidColorBrush(Color.FromArgb(255, 0, 0, 0)));
                }
            }


        }
    }
}
