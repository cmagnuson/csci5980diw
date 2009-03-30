using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace SilverlightRadialGraph.Library
{
    public class ConnectionExclusion
    {
        public IEntity Entity1 { get; set; }
        public IEntity Entity2 { get; set; }

        public ConnectionExclusion(IEntity e1, IEntity e2)
        {
            this.Entity1 = e1;
            this.Entity2 = e2;
        }
    }
}
