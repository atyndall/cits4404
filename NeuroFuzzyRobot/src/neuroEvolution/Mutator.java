package neuroEvolution;

import java.util.Vector;

public interface Mutator {

	public Vector<Genome> mutate(Vector<Genome> pop);
	
}
