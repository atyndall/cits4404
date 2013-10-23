package neuroEvolution;

import java.util.Vector;

public interface Selection {

	public Vector<Genome> select(Population pop);
	
}
