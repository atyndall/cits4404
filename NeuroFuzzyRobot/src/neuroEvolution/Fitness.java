package neuroEvolution;

public interface Fitness {

	public double fitness(Genome g);
	
	public double[] fitness(Population pop);
	
}
