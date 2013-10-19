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
import neuroEvolution.NEATMutator;
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
		Population pop = new Population(50,NeuroBot.numInputs,NeuroBot.numOutputs,1,6,10,0.75);
		RobocodeEngine eng = new RobocodeEngine(new File("../robocode"));
		Fitness f = new RoboFitness(eng,"sample.SittingDuck",10);
		
		Selection sel = new TournamentSelection(25,0.2,50);
		
		pop.calculateFitness(f);
		int numIter = 5;
		for(int i = 0; i < numIter; i++) {
			Vector<Genome> newGenes = sel.select(pop);
			pop = NEATMutator.mutate(newGenes,0.1);
			pop.calculateFitness(f);
		}
		System.out.println("Best Fit: " + bestFitness(pop));
		System.out.println("Average Fit: " + averageFitness(pop));
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
}
