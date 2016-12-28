/*****************************************************************************************
*Class Name: Graph
*
*Dependencies: 
*
*			
*
*Description: It consists of 3 classes. A) Graph which specifies all the properties of the graph 	
*			  such as index of the node, add edge
*			B) GraphNode which specifies all the properties of the node such as index, neighbours and shortest distance.
*			C) GraphEdge which specifies egde property
*
*
*Author: Suraj Kumar Didwania <dsuraj@hawk.iit.edu>
*
*************************************************************************************************/import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.soap.Node;

// Graph represents the graph of the matrix
 class Graph{
		 ArrayList<GraphNode> nodes;
		 ArrayList<String> stat;
		 Connection conn = null;
		 Statement stmt;
		 String query;
		public Graph(int numnodes) throws SQLException, InstantiationException, IllegalAccessException
		{
			nodes = new ArrayList<GraphNode>(numnodes);
			stat = new ArrayList<String>();
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nysubway","root","root");
			stmt = conn.createStatement();
			query = "select stop_id FROM stations";
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())
			{
				stat.add(rs.getString("stop_id").substring(0, 3));
				
			}
			Set<String> hs = new HashSet<>();
			hs.addAll(stat);
			stat.clear();
			stat.addAll(hs);
			nodes.add(new GraphNode(0));	
			for(int i=1;i<stat.size();i++)
			{
				nodes.add(new GraphNode(i));
			}
			System.out.println(nodes.size());
		}
		public GraphNode getindex(int index)
		{
			return nodes.get(index);
		}
		public void addEdge(int x,int y,int weight)
		{			
			GraphNode X = getindex(x); GraphNode Y = getindex(y);
			X.addedge(y, weight);
			Y.addedge(x, weight);
		}
		public void printGraph()
        {
            for (GraphNode node : nodes) {
                node.printneighbours();
            }
        }
		public int size() {
			// TODO Auto-generated method stub
			return nodes.size();
		}
	}
 //GraphNode of the graph represent each node
	class GraphNode implements Comparable<GraphNode>
	{
		public int index;
		public HashMap<Integer,GraphEdge> neighbours;
		public int shortestdistance;
		public GraphNode predec;
		public GraphNode(int i) {
			this.index = i;
			neighbours = new HashMap<Integer,GraphEdge>();
			shortestdistance = Integer.MAX_VALUE;
		}
		public void addedge(int neighbour,int weight)
		{
			if(neighbours.containsKey(neighbour))
			{
				GraphEdge edge = neighbours.get(neighbour);
				if(edge.weight > weight)
				{
					edge.weight = weight;
				}
			}
			else
			{
				neighbours.put(neighbour, new GraphEdge(neighbour,weight));
			}
		}
		public GraphNode getPredec() {
			return predec;
		}
		public void setPredec(GraphNode predec) {
			this.predec = predec;
		}
		public GraphEdge getEdge(String index)
		{
			return neighbours.get(index);
		}
		public int getindex()
		{
			return index;
		}
		public int getindex(int index)
		{
			return index;
		}
		public Collection<GraphEdge> getneighbours()
		{
			return neighbours.values();
		}
		public void printneighbours()
		{
			for(GraphEdge edge: neighbours.values())
			{
				System.out.println((index) + " " + (edge.neighbour) + " " + edge.weight);
			}
		}
		public int compareTo(GraphNode other) {
			if (shortestdistance < other.shortestdistance) {
                return -1;
            }
            else if (shortestdistance == other.shortestdistance) {
                return 0;
            }
            else {
                return 1;
            }
        }
	}	
	
	//GraphEdge of the graph represents each edge
	 class GraphEdge
	{
		public int neighbour;
		public int weight;
		public GraphEdge(int neighbour, int weight) {
			this.neighbour = neighbour;
			this.weight = weight;
		}	
	}