package neuroEvolution;

import java.util.Random;
import java.util.Arrays;
import java.util.Vector;

public class TournamentSelection implements Selection {


	private int k; //tournament size
	private double p; //selection pressure
	private int n;
	
	public TournamentSelection(int size, double selectionPressure, int numTournaments) {
		k = size;
		p = selectionPressure;
		n = numTournaments;
	}
	
	@Override
	public Vector<Genome> select(Population pop) {
		Vector<Genome> out = new Vector<Genome>();
		Vector<Genome> sample = new Vector<Genome>(pop.genes);
		for(int j = 0; j < n; j++) {
			Vector<Genome> pool = new Vector<Genome>();
			Random rand = new Random();
			for(int i = 0; i < k; i++) {
				int next = rand.nextInt(sample.size());
				pool.add(sample.get(next));
			}
			Object[] parray = pool.toArray();
			Genome[] tmp = Arrays.copyOf(parray, parray.length, Genome[].class);
			Arrays.sort(tmp, new GenomeComparator());
			Genome toAdd = null;
			for(int i = 0; i < tmp.length; i++) {
				if(rand.nextDouble() < p) {
					toAdd = tmp[i];
					break;
				}
			}
			if(toAdd == null)
				toAdd = tmp[tmp.length-1];
			out.add(toAdd);
			sample.remove(toAdd);
		}
		return out;
	}

}
