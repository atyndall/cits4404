package neuroFuzzy;

public class ConnectionGene implements java.io.Serializable {
	
	int out;
	int in;
	double weight;
	boolean enabled;
	int innov;
			
	public ConnectionGene(int out, int in, double weight, boolean enabled,
			int innov) {
		this.out = out;
		this.in = in;
		this.weight = weight;
		this.enabled = enabled;
		this.innov = innov;
	}
	
	public String toString() {
		return out + "->" + in;
	}
	
}
