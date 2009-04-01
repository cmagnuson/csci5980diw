using System.Windows;
using System.Windows.Input;

namespace Graphite.Demo
{
    public partial class WhitePage
    {
        public WhitePage()
        {
            InitializeComponent();
            graphite.NodeHovering += graphite_ShowInfo;
            graphite.NodeAdd += graphite_NodeAdd;
            graphite.NodeClick += graphite_NodeClick;
            graphite.NodeSelect1 += graphite_NodeSelect1;
            graphite.NodeSelect2 += graphite_NodeSelect2;
            graphite.MouseLeftButtonDown += graphite_MouseLeftButtonDown;
        }

        void graphite_NodeClick(object sender, NodeEventArgs e)
        {
            if (Keyboard.Modifiers == ModifierKeys.Control)
            {
                graphite.AttachRandomNodeTo(e.Node);
                e.Handled = true;
                return;
            }
            if (Keyboard.Modifiers == ModifierKeys.Alt)
            {
                graphite.DeleteNode(e.Node);
                return;
            }

        }

        void graphite_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            if (e.Handled) return;
            if (Keyboard.Modifiers == ModifierKeys.Control)
            {
                graphite.AttachRandomNodeTo(null);
                return;
            }
        }

        void graphite_NodeSelect2(object sender, NodeEventArgs e)
        {
            Notifier.Text = string.Format("Node '{0}' was selected. Click on 'Add edge' to a a new edge between the selected nodes.", e.Node.Title.ToString());
            AnimationExtentions.ApplyFade(this.Notifier);
            //graphite.HighlightNode(e.Node);
        }

        void graphite_NodeSelect1(object sender, NodeEventArgs e)
        {
            Notifier.Text = string.Format("Node '{0}' was selected. You need to select a second one in order to add a new edge.", e.Node.Title.ToString());
            AnimationExtentions.ApplyFade(this.Notifier);
            //graphite.HighlightNode(e.Node);
        }

        void graphite_NodeAdd(object sender, NodeEventArgs e)
        {
            Notifier.Text = string.Format("Node '{0}' added.", e.Node.Title.ToString());
            AnimationExtentions.ApplyFade(this.Notifier);
        }

        void graphite_ShowInfo(object sender, NodeEventArgs e)
        {
            Info.Text = e.Node.Info.ToString();
        }

        private void AddNewNode(object sender, RoutedEventArgs e)
        {
            if (TitleBox.Text.Trim().Length == 0) return;
            Node newNode = new Node { Info = InfoBox.Text.Trim(), Title = TitleBox.Text.Trim() };
            graphite.AddNode(newNode);
            if (graphite.Selected1 != null)
            {
                graphite.AddEdge(graphite.Selected1, newNode);
            }

        }

        private void AddNewEdge(object sender, RoutedEventArgs e)
        {
            graphite.AddEdgeToSelected();
        }
    }
}
