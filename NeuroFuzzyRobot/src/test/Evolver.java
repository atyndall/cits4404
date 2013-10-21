package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;


import neuro.NeuroBot;
import neuroEvolution.BattleListener;
import neuroEvolution.Fitness;
import neuroEvolution.Genome;
import neuroEvolution.Mutator;
import neuroEvolution.NEATMutator;
import neuroEvolution.NEATRecombiner;
import neuroEvolution.Population;
import neuroEvolution.RoboFitness;
import neuroEvolution.Selection;
import neuroEvolution.TournamentSelection;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class Evolver {
	
	public static void main(String[] args) {
		Population pop = new Population(20,NeuroBot.numInputs,NeuroBot.numOutputs,4,10,20,1.0);
		RobocodeEngine eng = new RobocodeEngine(new File("../robocode"));
		Fitness f = new RoboFitness(eng,"sample.SittingDuck",5);
		
		Selection sel = new TournamentSelection(2,0.8,10);
		Mutator m = new NEATMutator(0.1);
		
		
		int numIter = 100;
		double bestFit = Double.MIN_VALUE;
		Genome bestGenome = null;
		for(int i = 0; i <= numIter; i++) {
			pop.calculateFitness(f);
			System.out.println(i + " " + averageFitness(pop) + " " + bestFitness(pop));
			Genome best = pop.getBest();
			if(best.fit > bestFit) {
				bestFit = best.fit;
				bestGenome = best;
				saveGenome(bestGenome,"genome" + bestFit + ".ser");
			}
			
			
			Vector<Genome> newGenes = sel.select(pop);
			pop.generate(newGenes, new NEATRecombiner());
			pop.mutate(m);
		}
		pop.calculateFitness(f);
		Genome best = pop.getBest();
		if(best.fit > bestFit) {
			bestFit = best.fit;
			bestGenome = best;
			saveGenome(bestGenome,"genome" + bestFit + ".ser");
		}
		System.out.println("Best Fit: " + bestFit);
		saveGenome(bestGenome,"genome.ser");
		//System.out.println("Best Fit: " + bestFitness(pop));
		//System.out.println("Average Fit: " + averageFitness(pop));
	}
	
	public static double averageFitness(Population pop) {
		double totalFitness = 0;
		for(Genome g : pop.genes) {
			totalFitness += g.fit;
		}
		double averageFitness = totalFitness/pop.size;
		return averageFitness;
	}
	
	public static double bestFitness(Population pop) {
		double bestFitness = Double.MIN_VALUE;
		for(Genome g : pop.genes) {
			double fit = g.fit;
			if(fit > bestFitness)
				bestFitness = fit;
		}
		return bestFitness;
	}
	
	public static void saveGenome(Genome g, String genomeFile) {
		try {
			FileOutputStream fileOut = new FileOutputStream(genomeFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(g);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void printDoubleArray(double[] a) {
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if(i < a.length-1)
				System.out.print(",");
		}
		System.out.println();
	}
}
