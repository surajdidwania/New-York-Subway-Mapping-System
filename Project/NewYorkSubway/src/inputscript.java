import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class inputscript {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException  {
		// TODO Auto-generated method stub
			DBase db = new DBase();
			Connection conn;
				conn = db.connect("jdbc:mysql://localhost:3306/nysubway","root","root");
			db.importdata(conn,args[0]);
	}
}
	class DBase
	{
		public Connection connect(String db_connect_str, String db_userid, String db_password) throws InstantiationException, IllegalAccessException, ClassNotFoundException
		{
			Connection conn;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			try {
				conn = DriverManager.getConnection(db_connect_str,db_userid,db_password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn=null;
			}
			return conn;
		}
		public void importdata(Connection conn, String filename)
		{
			Statement stmt;
			String query;
			try{
			stmt = conn.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			query = "LOAD DATA INFILE '"+filename+
				    "' INTO TABLE routes fields terminated by ',';";
			stmt.executeUpdate(query);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				stmt=null;
			}
		}
	}
