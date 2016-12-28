

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class Vertex implements Comparable<Vertex> {

	public String Vername;
	public Edge[] adj;
	public int dist;
	public Vertex  predec;
	
	public Vertex(String name)
	{
		this.Vername = name;
		this.dist = (int) Double.POSITIVE_INFINITY;

	}
	
	public String getVername() {
		return Vername;
	}

	public void setVername(String vername) {
		Vername = vername;
	}

	public Edge[] getAdj() {
		return adj;
	}

	public void setAdj(Edge[] adj) {
		this.adj = adj;
	}

	public int getDist() {
		return dist;
	}

	public void setDist(int dist) {
		this.dist = dist;
	}

	public Vertex getPredec() {
		return predec;
	}

	public void setPredec(Vertex predec) {
		this.predec = predec;
	}

	public int compareTo(Vertex o)
	{
		return Double.compare(dist, o.dist);
	}
	public String toString(){
		return this.Vername;
	}

	
}
