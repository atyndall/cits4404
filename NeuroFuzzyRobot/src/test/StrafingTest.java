package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;


import neuro.NeuroBot;
import neuro.NeuroStrafingBot;
import neuro.NeuroTargetingBot;
import neuroEvolution.Fitness;
import neuroEvolution.Genome;
import neuroEvolution.Mutator;
import neuroEvolution.NEATMutator;
import neuroEvolution.NEATRecombiner;
import neuroEvolution.Population;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class StrafingTest {
	
	public static void main(String[] args) {
		String dataFileName = "lastData";
		
		RobocodeEngine eng = new RobocodeEngine(new File("../robocode"));
		//eng.setVisible(true);
		Fitness f = new SurvivalFitness(eng,"neuro.NeuroStrafingBot","ab.DengerousRoBatra 1.3",10);
		Population pop = new Population(30,NeuroStrafingBot.numInputs,NeuroStrafingBot.numOutputs,f);
		
		
		
		int numIter = 200;
		double bestFit = 0.0;
		Genome bestGenome = null;
		int i = 0;
		while(i < numIter) {
			//TODO reporting this fitness doesn't really make sense due to speciation
			System.out.println(i + " " + averageFitness(pop) + " " + bestFitness(pop));
			
			Genome best = pop.getBest();
			if(best.fit >= bestFit) {
				bestFit = best.fit;
				bestGenome = best;
				saveGenome(bestGenome,"genome" + bestFit + ".ser");
			}
			//System.out.println("Best Genome This Iteration: \n" + best);
			
			
			pop.nextGeneration(f, new NEATRecombiner(), new NEATMutator(0.1,0.0));
			//System.out.println("Finished a generation");
			//TODO crashing on 3rd generation
			i++;
		}
		System.out.println("Best Fit: " + bestFit);
		saveGenome(bestGenome,"genome.ser");
		//System.out.println("Best Fit: " + bestFitness(pop));
		//System.out.println("Average Fit: " + averageFitness(pop));
	}
	
	public static void printSelection(Vector<Genome> genes) {
		System.out.print("Selected: ");
		for(Genome g : genes) {
			System.out.print(g.fit + " ");
		}
		System.out.println();
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
