import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class writefile {
	static Graph graph;
	public static void main(String[] args) throws SQLException, InstantiationException, IllegalAccessException, ParseException {
		// TODO Auto-generated method stub
		List<String> data = new ArrayList<String>();
		Statement stmt;
		String query;
		String query1;
		ArrayList<String> tripid = new ArrayList<String>();
		ArrayList routeid = new ArrayList();
		ArrayList<String> arr_time = new ArrayList<String>();
		ArrayList<String> dep_time = new ArrayList<String>();
		ArrayList<String> stop_id = new ArrayList<String>();
		ResultSet rs3;
		ResultSet rs1;
		SimpleDateFormat simpleDateFormat;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nysubway","root","root");
		stmt = conn.createStatement();
		
		//query = "select DISTINCT trip_id from stop_timing";
		query = "select DISTINCT route_id FROM trips";
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next())
		{
			routeid.add(rs.getString("route_id"));	
		}
		for(int i=0;i<routeid.size();i++)
		{
			query = "select max(trip_id) from trips where route_id = '"+ routeid.get(i)+"'";
			ResultSet rs2 = stmt.executeQuery(query);
			while(rs2.next())
			{
				tripid.add(rs2.getString("max(trip_id)"));
			}
		}
		for(int i=0;i<tripid.size();i++)
		{
			ArrayList<Trip_Timings> tt = new ArrayList<Trip_Timings>();
			query = "select arrival_time,departure_time,stop_id,stop_sequence from stop_timing where trip_id ='"+ tripid.get(i)+"'";
			rs1 = stmt.executeQuery(query);
			System.out.println(tripid.get(i));
			while(rs1.next())
			{
				Trip_Timings timing= new Trip_Timings(rs1.getString("arrival_time"),rs1.getString("departure_time"),rs1.getString("stop_id"),rs1.getInt("stop_sequence"));
				//arr_time.add(rs1.getString("arrival_time"));
				//System.out.println(arr_time.get(i));
				//dep_time.add(rs1.getString("departure_time"));
				//System.out.println(dep_time.get(i));
				//stop_id.add(rs1.getString("stop_id"));
				//System.out.println(stop_id.get(i));
				//seq.add(rs1.getInt("stop_sequence"));
				tt.add(timing);
			}
			query = "select route_id from trips where trip_id = '"+ tripid.get(i)+"'";
			rs3 = stmt.executeQuery(query);
			while(rs3.next()){
			for(int j=0;j<tt.size()-1;j++)
			{
				simpleDateFormat = new SimpleDateFormat("HH:MM:SS");
				java.util.Date d1 = simpleDateFormat.parse(tt.get(j+1).arr_time);
				java.util.Date d2 = simpleDateFormat.parse(tt.get(j).dep_time);
				String[] time1 = (tt.get(j+1).arr_time).split(":");
				String[] time2 = (tt.get(j).dep_time).split(":");
				int time3 = (Math.multiplyExact(Integer.parseInt(time1[0]), 3600))+Math.multiplyExact(Integer.parseInt(time1[1]),60)+Integer.parseInt(time1[2]);
				int time4 = (Math.multiplyExact(Integer.parseInt(time2[0]), 3600))+Math.multiplyExact(Integer.parseInt(time2[1]),60)+Integer.parseInt(time2[2]);
				int duration = time3-time4;
				if(duration < 1000 && duration >=0)
				data.add(tt.get(j).stop_id.substring(0, 3) + ' ' + tt.get(j+1).stop_id.substring(0, 3)+ ' ' + rs3.getString("route_id") + ' ' + duration);
				else
					data.add(tt.get(j).stop_id.substring(0, 3) + ' ' + tt.get(j+1).stop_id.substring(0, 3)+ ' ' + rs3.getString("route_id") + ' ' +0);
			}
			}
		}
		writetofile(data,"C:/Users/Suraj Didwania/workspace/NewYorkSubway/input.txt");
		rs.close();
		stmt.close();
	}

	private static void writetofile(List data, String string) throws InstantiationException, IllegalAccessException, SQLException {
		// TODO Auto-generated method stub
		BufferedWriter out = null;
		File  file = new File(string);
			
		try {
			out = new BufferedWriter(new FileWriter(file,false));
			for (Object s : data) {
                out.write((String) s);
                out.newLine();

        }
			takinginputs();
        out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void takinginputs() throws FileNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		BufferedReader br = null;
		String line;
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
		graph = new Graph(val);
		File f = new File("input");
		br = new BufferedReader(new FileReader(f.getAbsolutePath()));
		try {
			while((line = br.readLine())!=null)
			{
				String[] value = line.split(" ");
				graph.addEdge(Integer.parseInt(value[0]), Integer.parseInt(value[1]), Integer.parseInt(value[3]));
				
			}
			graph.printGraph();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
