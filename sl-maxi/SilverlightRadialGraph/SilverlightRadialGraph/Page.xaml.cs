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
using SilverlightRadialGraph.Library;

namespace SilverlightRadialGraph
{
    public partial class Page : UserControl
    {
        Dictionary<IEntity, EntityControl> buttonRegistry = null;
        ScaleTransform scale;
        bool cancelScroll = false;
        IEntity newTarget = null;
        bool needInitialScroll = true;
        bool needInitialZoom = true;
        bool doReset = false;

        public Page()
        {
            InitializeComponent();
            this.Loaded += new RoutedEventHandler(Page_Loaded);
            scale = new ScaleTransform();
            this.entityCanvas.RenderTransform = scale;

        }

        void Page_Loaded(object sender, RoutedEventArgs e)
        {
        }

        void ResetClick(object sender, EventArgs e)
        {
            this.doReset = true;
            this.fadeOutStoryboard.Begin();

        }

        void Run()
        {
            IEntity entity = this.GetStartingEntity();
            this.LoadModel(entity);
            if (this.needInitialScroll)
            {
                this.Scroll(new Point(100, 100));
                this.needInitialScroll = false;
            }

            if (this.needInitialZoom)
            {
                this.DoZoom();
                this.needInitialZoom = false;
            }
        }

        IEntity GetStartingEntity()
        {
            NetworkGenerator generator = new NetworkGenerator();
            generator.Depth = int.Parse(this.depthTextBox.Text);
            generator.MaxChildren = int.Parse(this.maxChildrenTextBox.Text);
            generator.MaxRandomConnections = int.Parse(this.maxRandomConnectionsTextBox.Text);
            IEntity entity = generator.CreateNetwork();
            return entity;
        }

        void LoadModel(IEntity entity)
        {
            this.buttonRegistry = new Dictionary<IEntity, EntityControl>();
            this.entityCanvas.Children.Clear();
            int depth = int.Parse(this.depthTextBox.Text);

            EntityControl startControl = new EntityControl(entity);
            startControl.Title = entity.Name;
            ScaleTransform trans = new ScaleTransform();
            trans.ScaleX = 1.3;
            trans.ScaleY = 1.3;
            startControl.RenderTransform = trans;
            startControl.Color = Colors.Blue;
            this.entityCanvas.Children.Add(startControl);

            Canvas.SetLeft(startControl, this.entityCanvas.ActualWidth / 2);
            Canvas.SetTop(startControl, this.entityCanvas.ActualHeight / 2);

            this.buttonRegistry.Add(entity, startControl);

            List<IEntity> ringEntities = entity.Connections;
            for (int i = 1; i <= depth; i++)
            {
                ringEntities = this.LoadRing(ringEntities, i);
            }
            this.MakeConnections();
            this.infoBlock.Text = "Node Count: " + this.buttonRegistry.Count.ToString();
        }

        List<IEntity> LoadRing(List<IEntity> ringEntities, int depth)
        {
            List<IEntity> nextRing = new List<IEntity>();
            //1 radian = ~57.3 degrees
            //~6 radians / circle
            double radiusFactor = Convert.ToDouble(depth) * 1;
            double radius = 160 * radiusFactor;
            double stepConstant = .06 * Convert.ToDouble(spacingSlider.Value);
            double radianStep = stepConstant / Convert.ToDouble(ringEntities.Count);
            double radians = 0;
            this.infoBlock.Text = ringEntities.Count.ToString();

            foreach (IEntity entity in ringEntities)
            {
                Point point = this.CalculateRingPoint(radians, radius);
                Point relativePoint = this.TranslateRingPoint(point);
                EntityControl button = new EntityControl(entity);
                button.Clicked += new EventHandler<EntityControlClickedEventArgs>(button_Clicked);
                button.Opacity = 1 / (Convert.ToDouble(depth) * .3);
                button.Title = entity.Name;
                Canvas.SetLeft(button, relativePoint.X);
                Canvas.SetTop(button, relativePoint.Y);
                this.entityCanvas.Children.Add(button);
                this.buttonRegistry.Add(entity, button);
                radians = radians + radianStep;
            }

            foreach (IEntity entity in ringEntities)
            {
                foreach (IEntity nextConnection in entity.Connections)
                {
                    if (!this.buttonRegistry.ContainsKey(nextConnection))
                    {
                        if (!nextRing.Contains(nextConnection))
                        {
                            nextRing.Add(nextConnection);
                        }
                    }
                }
            }


            return nextRing;
        }

        void button_Clicked(object sender, EntityControlClickedEventArgs e)
        {
            this.cancelScroll = true;
            this.fadeOutStoryboard.Begin();
            this.newTarget = e.Entity;
        }

        void MakeConnections()
        {
            Dictionary<int, int> exclusions = new Dictionary<int, int>();
            foreach (IEntity entity in this.buttonRegistry.Keys)
            {
                foreach (IEntity connection in entity.Connections)
                {
                    this.EvaluateConnection(entity, connection, exclusions);                    
                }
            }
        }

        void EvaluateConnection(IEntity entity, IEntity connection, Dictionary<int, int> exclusions)
        {
            int key = entity.GetHashCode() * connection.GetHashCode();
            if (!exclusions.ContainsKey(key))
            {
                if (this.buttonRegistry.ContainsKey(connection))
                {
                    this.DrawConnection(entity, connection);
                    exclusions.Add(key, 0);
                }
            }
        }

        void DrawConnection(IEntity entity1, IEntity entity2)
        {
            EntityControl button1 = this.buttonRegistry[entity1];
            EntityControl button2 = this.buttonRegistry[entity2];


            Line line = new Line();
            line.StrokeThickness = 1;
            line.Stroke = new SolidColorBrush(Colors.Black);
            line.Opacity = .5;
            line.X1 = Canvas.GetLeft(button1) + button1.Width / 2;
            line.Y1 = Canvas.GetTop(button1) + button1.Height / 2;
            line.X2 = Canvas.GetLeft(button2) + button2.Width / 2;
            line.Y2 = Canvas.GetTop(button2) + button2.Height / 2;
            button1.ConnectedControls.Add(button2);
            button2.ConnectedControls.Add(button1);
            button1.ConnectionLines.Add(line);
            button2.ConnectionLines.Add(line);
            this.entityCanvas.Children.Insert(0, line);

        }

        Point CalculateRingPoint(double radians, double radius)
        {
            double x = Math.Cos(radians) * radius;
            double y = Math.Sin(radians) * radius;
            return new Point(x, y);
        }

        Point TranslateRingPoint(Point source)
        {
            double centerWidth = this.entityCanvas.ActualWidth / 2;
            double centerHeight = this.entityCanvas.ActualHeight / 2;

            double relativeX = source.X + centerWidth;
            double relativeY = source.Y + centerHeight;
            return new Point(relativeX, relativeY);
        }


        protected void MainCanvasClick(object sender, MouseButtonEventArgs e)
        {
            if (this.cancelScroll)
            {
                this.cancelScroll = false;
                return;
            }
            Point clickPoint = e.GetPosition(this.mainCanvas);
            this.Scroll(clickPoint);
        }

        void Scroll(Point clickPoint)
        {
            Point midPoint = new Point(this.mainCanvas.ActualWidth / 2, this.mainCanvas.ActualHeight / 2);
            Point difference = new Point(midPoint.X - clickPoint.X, midPoint.Y - clickPoint.Y);

            double currentLeft = Canvas.GetLeft(this.entityCanvas);
            double currentTop = Canvas.GetTop(this.entityCanvas);

            double newLeft = currentLeft + difference.X;
            double newTop = currentTop + difference.Y;

            this.leftAnimation.To = newLeft;
            this.topAnimation.To = newTop;

            this.leftAnimationStoryboard.Begin();
            this.topAnimationStoryboard.Begin();
        }

       

        private void zoomSlider_ValueChanged(object sender, RoutedPropertyChangedEventArgs<double> e)
        {
            if (this.zoomSlider != null)
            {
                this.DoZoom();
            }

        }

        void DoZoom()
        {
            double factor = this.zoomSlider.Value / 50;
            this.scale.ScaleX = factor;
            this.scale.ScaleY = factor;
        }

        private void fadeOutStoryboard_Completed(object sender, EventArgs e)
        {
            if (this.doReset)
            {
                this.doReset = false;
                this.Run();
            }
            else
            {
                this.LoadModel(newTarget);
            }
            this.fadeInStoryboard.Begin();

        }

    }
}
