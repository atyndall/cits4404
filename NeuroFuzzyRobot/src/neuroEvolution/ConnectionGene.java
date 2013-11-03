package neuroEvolution;

public class ConnectionGene implements java.io.Serializable, java.lang.Comparable {
	
	public int out;
	public int in;
	public double weight;
	public boolean enabled;
	public int innov;
			
	public ConnectionGene(int out, int in, double weight, boolean enabled,
			int innov) {
		this.out = out;
		this.in = in;
		this.weight = weight;
		this.enabled = enabled;
		this.innov = innov;
	}
	
	@Override
	public boolean equals(Object obj) {
		ConnectionGene g = (ConnectionGene)obj;
		return out == g.out && in == g.in;
	}
	
	public String toString() {
		return out + "->"  + in + "(" + innov + ")";
	}
	
	public void disable() {
		enabled = false;
	}

	@Override
	public int compareTo(Object o) {
		ConnectionGene g = (ConnectionGene)o;
		return this.innov - g.innov;
	}
	
	@Override
	public ConnectionGene clone() {
		return new ConnectionGene(out,in,weight,enabled,innov);
	}
}
