package neuroEvolution;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import neuro.NeuroBot;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class RoboFitness implements Fitness {
	
	public final static String genomeFile = "../robocode/robots/neuro/NeuroBot.data/genome.ser";
	
	RobocodeEngine eng;
	String testRobo;
	int numRounds;
	
	public RoboFitness(RobocodeEngine eng, String testRobo, int numRounds) {
		this.eng = eng;
		this.testRobo = testRobo;
		this.numRounds = numRounds;
	}

	@Override
	public double fitness(Genome g) {
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
		
		
		BattleListener reporter = new BattleListener();
		eng.addBattleListener(reporter);
		RobotSpecification[] robots = eng.getLocalRepository("neuro.NeuroBot*," + testRobo);
		eng.runBattle(new BattleSpecification(numRounds, new BattlefieldSpecification(), robots), true);
		System.out.println(reporter.result[0].getScore());
		return reporter.result[0].getScore();
	}

	@Override
	public double[] fitness(Population pop) {
		// TODO Auto-generated method stub
		return null;
	}

}
