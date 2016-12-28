import java.sql.Time;

public class Trip_Timings {
		String arr_time;
		String dep_time;
		String stop_id;
		int seq;
		Trip_Timings(String arr_time,String dep_time,String stop_id,int seq)
		{
			this.arr_time = arr_time;
			this.dep_time = dep_time;
			this.stop_id = stop_id;
			this.seq = seq;
		}
}
