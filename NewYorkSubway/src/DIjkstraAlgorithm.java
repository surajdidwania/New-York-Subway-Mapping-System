import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class DIjkstraAlgorithm {
	static Graph graph;
	static int[] shortestDistance;
	static ArrayList<String> stat;
	static int startid;
	static int destid;
	static ArrayList<String> stopage;
		static ArrayList<String> destin;
	public static void main(String[] args) throws FileNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// TODO Auto-generated method stub
		String query,query1;
		Connection conn = null;
		 Statement stmt;
		 stopage = new ArrayList<String>();
		 destin = new ArrayList<String>();
		takinginputs();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nysubway","root","root");
		stmt = conn.createStatement();
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the source station ");
		String start  = sc.nextLine();
		query = "select stop_id from stations where stop_name ='"+start+"'";
		ResultSet rs6 = stmt.executeQuery(query);	
		System.out.println("Please select the stop_id from the below source stations");
		while(rs6.next()){
		stopage.add(rs6.getString("stop_id"));}
		for(int i=0;i<stopage.size();i++)
		{
			if(i%3 == 0)
			System.out.println(stopage.get(i));
		}
		String stop_id = sc.nextLine();
		 startid = getCategoryPos(stop_id.substring(0, 3));
		 
		 
		System.out.println("Please enter the destination station ");
		String dest = sc.nextLine();
		query1 = "select stop_id from stations where stop_name ='"+dest+"'";
		ResultSet rs7 = stmt.executeQuery(query1);	
		System.out.println("Please select the stop_id from the below destination stations");
		while(rs7.next()){
			destin.add(rs7.getString("stop_id"));}
		for(int i=0;i<destin.size();i++)
		{
			if(i%3 == 0)
			System.out.println(destin.get(i));
		}
		String stop_id1 = sc.nextLine();
		destid = getCategoryPos(stop_id1.substring(0, 3));
		GraphNode destination = graph.getindex(destid);
		int distance = dijkstraShortestDistance(graph,startid,destid);
		List path= shorterpath(destination);
		
		for(int i=0;i<path.size();i++){
			//System.out.println(stat.get((int) path.get(i)));
			query = "select stop_name FROM stations where stop_id ='"+ stat.get((int) path.get(i))+"'";
			ResultSet rs8 = stmt.executeQuery(query);	
			while(rs8.next())
			{
				if(i<path.size()-1)
				System.out.print(rs8.getString("stop_name") + " " + stat.get((int) path.get(i))+ " " + "->");
				else
					System.out.print(rs8.getString("stop_name") + " " +stat.get((int) path.get(i)));
			}
		}
		System.out.println();
		//int hr = distance/3600;
		float min = distance/60;
		//if((distance/3600)>1){
		// min = (distance/60)-60;}else { min = (distance/60);}
		//int sec = 
		System.out.println("Total time to travel from " +start + " to " + dest + " is " +min + "minutes");

	}
	public static void takinginputs() throws FileNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		BufferedReader br = null;
		String line;
		stat = new ArrayList<String>();
		Connection conn = null;
		Statement stmt;
		
		String query;
		int val = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nysubway","root","root");
		stmt = conn.createStatement();
		query = "select count(*) FROM stations";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next())
		{
			 val = rs.getInt("count(*)");
		}
		graph = new Graph(val/3);
		File f = new File("input.txt");
		query = "select stop_id FROM stations";
		ResultSet rs4 = stmt.executeQuery(query);
		while(rs4.next())
		{
			stat.add(rs4.getString("stop_id").substring(0, 3));
			
		}
		Set<String> hs = new HashSet<>();
		hs.addAll(stat);
		stat.clear();
		stat.addAll(hs);
		br = new BufferedReader(new FileReader(f.getAbsolutePath()));
		try {
			while((line = br.readLine())!=null)
			{
				String[] value = line.split(" ");
				int pos1 = getCategoryPos(value[0]);
				int pos2 = getCategoryPos(value[1]);
				graph.addEdge(pos1,pos2, Integer.parseInt(value[3]));	
			}
			//graph.printGraph();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getCategoryPos(String category) {
		  return stat.indexOf(category);
		}
	private static int dijkstraShortestDistance(Graph graph, int start,int dest) throws InstantiationException, IllegalAccessException, SQLException
    {
		Connection conn = null;
		Statement stmt;
		String query;
        boolean[] visited = new boolean[graph.size()];
        for(int i=0;i<graph.size();i++)
        {
        	visited[i] = false;
        }
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
            //visited[rs.getString("stop_id")] = false;
        }
        PriorityQueue<GraphNode> q = new PriorityQueue<GraphNode>();
        
        graph.getindex(start).shortestdistance = 0;
        q.add(graph.getindex(start));
        
        while (!q.isEmpty()) {
            GraphNode node = q.poll();
            visited[node.getindex()] = true;
            
            // For every neighbor         
            for (GraphEdge edge : node.getneighbours()) {
                GraphNode neighbour = graph.getindex(edge.neighbour);
                
                // If not visited, recalculate shortest distance to neighbor
                if (!visited[neighbour.getindex()]) {
                    int distance = node.shortestdistance + edge.weight;
                    if(distance < neighbour.shortestdistance)
                    {
                    neighbour.shortestdistance = Math.min(neighbour.shortestdistance, distance);
                    neighbour.setPredec(node);
                    //System.out.println(neighbour.getPredec());
                    // Add to queue
                    if (!q.contains(neighbour)) {
                        q.add(neighbour);
                    }
                    }
                }
            }
        }
     // Put shortest distances into array, -1 if not reachable
        shortestDistance= new int[graph.size()];
        for (int i=0; i<shortestDistance.length; i++) {
            if (graph.getindex(i).shortestdistance != Integer.MAX_VALUE) {
                shortestDistance[i] = graph.getindex(i).shortestdistance;
            }
            else {
                shortestDistance[i] = -1;
            }
        }
        return shortestDistance[dest];
    }
	public static List shorterpath(GraphNode target){
		List path = new ArrayList();
		for(GraphNode v = target; v != null; v = v.getPredec()){
			int node = v.getindex();
			path.add(node);
		}
		Collections.reverse(path);
		return path;
	}

}
