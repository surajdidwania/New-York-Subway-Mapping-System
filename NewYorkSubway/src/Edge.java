

public class Edge {
		public Vertex target;
		public int weight;
		Edge(Vertex target, int weight)
		{
			this.target = target;
			this.weight = weight;
		}
		public void setWeight(int weight)
		{
				this.weight = weight;
		}
		public Vertex getTarget() {
			return target;
		}
		public int getWeight() {
			return weight;
		}	
		public void setTarget(Vertex target) {
			this.target = target;
		}
}
