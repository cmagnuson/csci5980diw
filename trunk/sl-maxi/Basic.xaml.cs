using System.Windows;

namespace Graphite.Demo
{
    public partial class Basic
    {
        public Basic()
        {
            InitializeComponent();
            graphite.Loaded += Graphite_OnLoaded;
        }

        private void Graphite_OnLoaded(object sender, RoutedEventArgs e)
        {
            //Warning: if you wish to read data from the network or from disk,
            //you should read the security constraints in Silverlight first!

            //You can load XML data from a webserver like this
            //graphite.LoadGraphFromXml("http://localhost:6777/SampleGraph.xml");
            //or flat file data
            //graphite.LoadGraphFromFlatFile("http://localhost:6777/SampleGraph.txt");
            
            
            graphite.LoadSampleGraph();
            
            //you can also move nodes when the layout is not active
            //graphite.StopLayout();
        }


    }
}
