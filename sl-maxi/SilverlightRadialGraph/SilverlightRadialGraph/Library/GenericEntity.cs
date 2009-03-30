using System;
using System.Collections.Generic;
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
    public class GenericEntity : IEntity
    {
        public string Name { get; set; }
        public List<IEntity> Connections { get; set; }

        public GenericEntity()
        {
            this.Connections = new List<IEntity>();
        }

        public GenericEntity(string name)
        {
            this.Connections = new List<IEntity>();
            this.Name = name;
        }
    }
}
