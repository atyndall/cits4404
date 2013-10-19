package neuroEvolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NEATRecombiner implements Recombination {

	static double enableChance = 0.1;
	
	@Override
	public Genome recombine(Genome p1, Genome p2) {
		ConnectionGene[] c1 = new ConnectionGene[p1.connections.size()];
		ConnectionGene[] c2 = new ConnectionGene[p2.connections.size()];
		c1 = p1.connections.toArray(c1);
		c2 = p2.connections.toArray(c2);
		Arrays.sort(c1);
		Arrays.sort(c2);
		int i = 0;
		int j = 0;
		Random rand = new Random();
		List<ConnectionGene> newConns = new ArrayList<ConnectionGene>();
		//TODO figure out some way of bringing the nodes across
		//Might be better to recreate all the nodes out of the connections
		List<NodeGene> nodes = new ArrayList<NodeGene>();
		while(i < c1.length && j < c2.length) {
			ConnectionGene toAdd = null;
			if(c1[i].innov == c2[j].innov) {
				if(rand.nextBoolean())
					newConns.add(c1[i]);
				else
					newConns.add(c2[j]);
				i++;
				j++;
			//if c2 is missing some genes
			} else if(c1[i].innov < c2[j].innov) {
				if(p1.fit >= p2.fit)
					newConns.add(c1[i]);
				i++;
			//if c1 is missing some genes
			} else {
				if(p2.fit >= p1.fit)
					newConns.add(c2[j]);
				j++;
			}
		}
		//Do any excess genes
		while(i < c1.length && p1.fit >= p2.fit) {
			newConns.add(c1[i]);
		}
		while(j < c2.length && p2.fit >= p1.fit) {
			newConns.add(c2[j]);
		}
		
		return null;
	}

}
