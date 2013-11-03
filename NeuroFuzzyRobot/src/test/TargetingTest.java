package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;


import neuro.NeuroBot;
import neuro.NeuroTargetingBot;
import neuroEvolution.Fitness;
import neuroEvolution.Genome;
import neuroEvolution.NEATMutator;
import neuroEvolution.NEATRecombiner;
import neuroEvolution.NodeGene;
import neuroEvolution.Population;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class TargetingTest {
	
	public static void main(String[] args) {
		String dataFileName = "lastData";
		
		RobocodeEngine eng = new RobocodeEngine(new File("../robocode"));
		//eng.setVisible(true);
		Fitness f = new AccuracyFitness(eng,"neuro.NeuroTargetingBot","sample.SittingDuck",5);
		Population pop = new Population(80,NeuroTargetingBot.numInputs,NeuroTargetingBot.numOutputs,f);
		//System.out.println(0 + " " + averageFitness(pop) + " " + bestFitness(pop));
		
		
		int numIter = 1000;
		double bestFit = Double.MIN_VALUE;
		Genome bestGenome = null;
		int i = 0;
		do {
			//TODO reporting this fitness doesn't really make sense due to speciation
			System.out.println(i + " " + averageFitness(pop) + " " 
					+ bestFitness(pop) + " " + averageHiddenNeurons(pop) + " " + averageNumConnectionGenes(pop));
			
			Genome best = pop.getBest();
			if(best.fit > bestFit) {
				bestFit = best.fit;
				bestGenome = best;
				saveGenome(bestGenome,"genome" + bestFit + ".ser");
			}
			//System.out.println("Best Genome This Iteration: \n" + best);
			
			
			pop.nextGeneration(f, new NEATRecombiner(), new NEATMutator(0.1,0.7));
			//System.out.println("Finished a generation");
			//TODO crashing on 3rd generation
			i++;
		} while(i < numIter && averageFitness(pop) < 0.5);
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
	
	public static double averageHiddenNeurons(Population pop) {
		double average = 0;
		for(Genome g : pop.genes) {
			for(NodeGene n : g.nodes) {
				if(n.type == NodeGene.HIDDEN)
					average += 1.0;
			}
		}
		average = average/pop.genes.size();
		return average;
	}
	
	public static double averageNumConnectionGenes(Population pop) {
		double average = 0;
		for(Genome g : pop.genes) {
			average += g.connections.size();
		}
		average = average/pop.genes.size();
		return average;
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
