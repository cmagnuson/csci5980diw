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

namespace SilverlightMaxi
{
    public partial class Page : UserControl
    {
        public Page()
        {
            InitializeComponent();
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
