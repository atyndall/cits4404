package neuroEvolution;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import neuro.NeuroBot;
import neuroFuzzy.Util;


public class Population {

	public List<Genome> genes;
	public List<List<Genome>> species;
	List<Genome> lastReps;
	public int size;
	
	
	
	public Population(List<Genome> genes) {
		this.genes = new ArrayList<Genome>(genes);
		size = genes.size();
	}
	
	public Population(int popSize, int numInputs, int numOutputs, Fitness f) {
		size = popSize;
		genes = new ArrayList<Genome>();
		for(int i = 0; i < popSize; i++) {
			genes.add(new Genome(numInputs,numOutputs));
		}
		species = new ArrayList<List<Genome>>();
		lastReps = new ArrayList<Genome>();
		
		//first time setup
		//speciate();
		//System.out.println("Num Species: " + species.size());
		calculateFitness(f);
	}	
	
	double truncationThreshold = 0.6;
	
	public void nextGeneration(Fitness f,  NEATRecombiner r, NEATMutator m) {
		speciate();
		speciateFitness();
		//System.out.println("Num Species: " + species.size());
		
		//determine the number of offspring
		//Each species make offspring equal to their proportion of the total fitness
		double[] speciesFitness = new double[species.size()];
		double totalFitness = 0;
		for(int i = 0; i < species.size(); i++) {
			speciesFitness[i] = 0;
			for(int j = 0; j < species.get(i).size(); j++) {
				speciesFitness[i] += species.get(i).get(j).fit;
				totalFitness += species.get(i).get(j).fit;
			}
		}
		
		int[] numOffspring = new int[species.size()];
		int remainingOffspring = this.size;
		for(int i = 0; i < numOffspring.length; i++) {
			if(speciesFitness[i] > 0)
				numOffspring[i] = Math.max(1,(int)((speciesFitness[i]/totalFitness)*this.size));
			remainingOffspring -= numOffspring[i];
		}
		assert(remainingOffspring >= 0);
		//divide up the remaining offspring
		int o = 0;
		//TODO this looping forever sometimes
		while(remainingOffspring > 0) {
			int num = Math.min(remainingOffspring,(int)((speciesFitness[o]/totalFitness)*remainingOffspring));
			if(num < 1)
				num = 1;
			numOffspring[o] += num;
			remainingOffspring -= num;
			o = (o+1)%speciesFitness.length;
		}
		//System.out.println("Num Offspring per Species:");
		//printIntArray(numOffspring);
		//System.out.println();
		
		//reproduce
		List<Genome> newGenes = new ArrayList<Genome>();
		for(int i = 0; i < species.size(); i++) {
			Genome[] sortedGenes = new Genome[species.get(i).size()];
			sortedGenes = species.get(i).toArray(sortedGenes);
			Arrays.sort(sortedGenes, new GenomeComparator());
			List<Genome> selection = new ArrayList<Genome>();
			for(int j = 0; j < Math.max(1,sortedGenes.length*truncationThreshold); j++) {
				selection.add(sortedGenes[j]);
			}
			
			//save the champion
			if(species.get(i).size() > 5 && numOffspring[i] > 0) {
				newGenes.add(sortedGenes[0]);
				numOffspring[i]--;
			}
			
			for(int j = 0; j < numOffspring[i]; j++) {
				newGenes.add(r.recombine(select(selection), select(selection)));
			}
		}
		assert(newGenes.size() == this.size);
		//mutate
		genes = m.mutate(newGenes);
		//TODO possibly a cleanup here
		//some nodes are getting possible outputs from outside their genome
		calculateFitness(f);

		//System.out.println("Num Species: " + species.size());
		
	}
	
	
	
	public static <E> E select(List<E> list) {
		return list.get(Util.uniformInt(0, list.size()-1));
	}
	
	public void printIntArray(int[] a) {
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}
	
	double speciationThreshold = 3.0;
	
	private void speciate() {
		ArrayList<List<Genome>> species = new ArrayList<List<Genome>>();
		
		List<Genome> genesCopy = new ArrayList<Genome>(genes);
		for(int i = 0; i < lastReps.size(); i++) {
			species.add(new ArrayList<Genome>());
		}
		//speciate new genes
		for(Genome g : genesCopy) {
			int specIndex = -1;
			for(int i = 0; i < lastReps.size(); i++) {
				if(genomeDistance(g,lastReps.get(i)) < speciationThreshold) {
					specIndex = i;
					break;
				}
			}
			if(specIndex < 0) {
				List<Genome> newSpec = new ArrayList<Genome>();
				newSpec.add(g);
				species.add(newSpec);
				lastReps.add(g);
			} else
				species.get(specIndex).add(g);
		}
		//prune empty species
		//and generate representatives
		List<Genome> newReps = new ArrayList<Genome>();
		this.species = new ArrayList<List<Genome>>();
		for(int i = 0; i < species.size(); i++) {
			if(species.get(i).size() != 0) {
				this.species.add(species.get(i));
				newReps.add(species.get(i).get(0));
			}
		}
		lastReps = newReps;
		assert(this.species.size() == lastReps.size());
	}
	
	/**
	 * Calculates speciation fitness scaling
	 * for doing selection
	 */
	private void speciateFitness() {
		for(int i = 0; i < species.size(); i++) {
			for(int j = 0; j < species.get(i).size(); j++) {
				Genome g = species.get(i).get(j);
				g.fit = g.fit/species.get(i).size();
			}
		}
	}
	
	/**
	 * Calculates raw fitness without speciation
	 * @param f
	 */
	private void calculateFitness(Fitness f) {
		//
		for(int i = 0; i < genes.size(); i++) {
		
			Genome g = genes.get(i);
			g.fit = f.fitness(g);
			//System.out.println(g.fit);
			
		}
		
		//System.out.println();
	}
	
	double excessWeight = 1.0;
	double disjointWeight = 1.0;
	double matchingWeight = 0.4;
	
	public double genomeDistance(Genome g1, Genome g2) {
		ConnectionGene[] c1 = g1.getSortedGeneList();
		ConnectionGene[] c2 = g2.getSortedGeneList();
		int i = 0;
		int j = 0;
		//TODO figure out some way of bringing the nodes across
		//Might be better to recreate all the nodes out of the connections
		double totalWeights = 0;
		int numMatches = 0;
		int numExcess = 0;
		int numDisjoint = 0;
		while(i < c1.length && j < c2.length) {
			if(c1[i].innov == c2[j].innov) {
				totalWeights += Math.abs(c1[i].weight - c2[j].weight);
				numMatches++;
				i++;
				j++;
			//if c2 is missing some genes
			} else if(c1[i].innov < c2[j].innov) {
				numDisjoint++;
				i++;
			//if c1 is missing some genes
			} else {
				numDisjoint++;
				j++;
			}
		}
		//Do any excess genes
		while(i < c1.length) {
			numExcess++;
			i++;
		}
		while(j < c2.length && g2.fit >= g1.fit) {
			numExcess++;
			j++;
		}
		//TODO paper says we can set N to 1 if genomes are small (less than 20 genes)
		double N = 1;
		if(Math.max(c1.length,c2.length) >= 20) {
			N = Math.max(c1.length,c2.length);
		}
		return (excessWeight*numExcess)/N + (disjointWeight*numDisjoint)/N + matchingWeight*(totalWeights/numMatches);
	}
	

	
	public Genome getBest() {
		double bestFit = Double.MIN_VALUE;
		Genome best = genes.get(0);
		for(Genome g : genes) {
			if(g.fit > bestFit) {
				bestFit = g.fit;
				best = g;
			}
		}
		return best;
	}
	
	public double[] getScores() {
		double[] out = new double[genes.size()];
		for(int i = 0; i < genes.size(); i++)
			out[i] = genes.get(i).fit;
		return out;
	}
	
	
	
}
