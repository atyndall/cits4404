package neuroEvolution;

import java.util.Comparator;

public class GenomeComparator implements Comparator<Genome> {

	@Override
	public int compare(Genome g1, Genome g2) {
		return (int)Math.signum(g2.fit - g1.fit);
	}

}
