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
    public class NetworkGenerator
    {
        Random random = new Random();
        List<IEntity> entityRegister;
        List<string> names;
        int depth = 6;
        int maxRandomConnections = 3;
        int maxChildren = 3;

        public NetworkGenerator()
        {
            names = this.LoadNames();
        }

        public int MaxRandomConnections
        {
            get { return this.maxRandomConnections; }
            set { this.maxRandomConnections = value; }
        }

        public int MaxChildren
        {
            get { return this.maxChildren; }
            set { this.maxChildren = value; }
        }

        public int Depth
        {
            get { return this.depth; }
            set { this.depth = value; }
        }

        public IEntity CreateNetwork()
        {
            this.entityRegister = new List<IEntity>();
            IEntity entity = this.CreateEntityTreeNode(1, this.Depth);
            this.CreateNetworkConnections();
            return entity;
        }

        IEntity CreateEntityTreeNode(int depth, int maxDepth)
        {
            string name = this.GetRandomName();
            GenericEntity newEntity = new GenericEntity(name);
            this.entityRegister.Add(newEntity);

            if (depth < maxDepth)
            {
                int next = random.Next(1, this.MaxChildren);
                for (int i = 0; i < next; i++)
                {
                    IEntity connection = this.CreateEntityTreeNode(depth + 1, maxDepth);
                    newEntity.Connections.Add(connection);
                    connection.Connections.Add(newEntity);
                }
            }
            return newEntity;
        }

        void CreateNetworkConnections()
        {
            foreach (IEntity entity in this.entityRegister)
            {
                this.CreateRandomConnections(entity);
            }
        }

        void CreateRandomConnections(IEntity entity)
        {
            int count = random.Next(0, this.MaxRandomConnections);
            if (count == 0)
            {
                return;
            }

            for (int i = 0; i < count; i++)
            {
                int index = random.Next(this.entityRegister.Count);
                IEntity target = this.entityRegister[index];
                this.TryAssociation(entity, target);
            }
        }

        void TryAssociation(IEntity entity, IEntity target)
        {
            if (entity == target)
            {
                return;
            }

            if (entity.Connections.Contains(target))
            {
                return;
            }

            if (target.Connections.Contains(entity))
            {
                return;
            }

            entity.Connections.Add(target);
            target.Connections.Add(entity);
        }

        string GetRandomName()
        {
            int index = random.Next(names.Count);
            string part1 = names[index];
            index = random.Next(names.Count);
            string part2 = names[index];
            return string.Format("{0} {1}", part1, part2);
        }

        List<string> LoadNames()
        {
            List<string> nameValues = new List<string>();
            nameValues.Add("Jester");
            nameValues.Add("Matt");
            nameValues.Add("Rob");
            nameValues.Add("Tim");
            nameValues.Add("Pete");
            nameValues.Add("Jane");
            nameValues.Add("Jen");
            nameValues.Add("Tia");
            nameValues.Add("Tina");
            nameValues.Add("Roy");
            nameValues.Add("Nat");
            nameValues.Add("Mark");
            nameValues.Add("Ken");
            nameValues.Add("Ler");
            nameValues.Add("Fred");
            nameValues.Add("Dave");
            nameValues.Add("Ed");
            nameValues.Add("Phil");
            nameValues.Add("Neil");
            nameValues.Add("Mike");
            nameValues.Add("Ray");
            nameValues.Add("Erik");
            nameValues.Add("Wes");
            nameValues.Add("Vish");
            nameValues.Add("Kip");
            nameValues.Add("Doug");
            nameValues.Add("Abe");
            nameValues.Add("Vin");
            nameValues.Add("Lori");
            nameValues.Add("Dori");
            nameValues.Add("Alex");
            nameValues.Add("Pat");
            nameValues.Add("Keri");
            nameValues.Add("Cari");
            nameValues.Add("Sam");
            nameValues.Add("Kris");
            nameValues.Add("Paul");
            nameValues.Add("Eddy");
            nameValues.Add("Buzz");
            nameValues.Add("Megan");
            nameValues.Add("Ali");

            return nameValues;
        }
    }
}
