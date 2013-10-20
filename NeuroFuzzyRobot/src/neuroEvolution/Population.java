package neuroEvolution;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import neuro.NeuroBot;


public class Population {

	public Vector<Genome> genes;
	public int size;
	
	public double totalFitness;
	
	public Population(Vector<Genome> genes) {
		this.genes = new Vector<Genome>(genes);
		size = genes.size();
	}
	
	public Population(int popSize, int numInputs, int numOutputs, int maxHiddenLayers, 
			int minHiddenNodes, int maxHiddenNodes, double connectionChance) {
		size = popSize;
		genes = new Vector<Genome>();
		for(int i = 0; i < popSize; i++) {
			genes.add(new Genome(numInputs,numOutputs,maxHiddenLayers,minHiddenNodes,maxHiddenNodes,connectionChance));
		}
			
	}	
		
	public void generate(Vector<Genome> newGenes, Recombination r) {
		Random rand = new Random();
		genes = new Vector<Genome>(newGenes);
		int numToGen = size - newGenes.size();
		int i = 0;
		while(i < numToGen) {
			int n1 = rand.nextInt(newGenes.size());
			int n2 = rand.nextInt(newGenes.size());
			Genome child = r.recombine(newGenes.get(n1), newGenes.get(n2));
			genes.add(child);
			i++;
		}
		
	}
	
	public void calculateFitness(Fitness f) {
		totalFitness = 0;
		for(Genome g : genes) {
			double fit = f.fitness(g);
			totalFitness += fit;
			g.setFit(fit);
		}
		System.out.println();
	}
	
	public Genome getBest() {
		double bestFit = Double.MIN_VALUE;
		Genome best = null;
		for(Genome g : genes) {
			if(g.fit > bestFit)
				best = g;
		}
		return best;
	}
	
	
	
}
