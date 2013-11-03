package test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import neuro.NeuroBot;
import neuro.NeuroTargetingBot;
import neuroEvolution.Fitness;
import neuroEvolution.Genome;
import neuroEvolution.Population;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class TargetingFitness implements Fitness {
	
	String robotName = "NeuroTargetingBot";
	String genomeFile = "../robocode/robots/neuro/" + robotName + ".data/genome.ser";
	
	RobocodeEngine eng;
	String testRobo;
	String sampleRobo;
	int numRounds;
	
	public TargetingFitness(RobocodeEngine eng, String testRobo, String sampleRobo, int numRounds) {
		//split because package name
		String[] tmp = testRobo.split("\\.");
		robotName = tmp[tmp.length-1];
		genomeFile = "../robocode/robots/neuro/" + robotName + ".data/genome.ser";
		this.eng = eng;
		this.testRobo = testRobo;
		this.sampleRobo = sampleRobo;
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
		//* for our robots
		RobotSpecification[] robots = eng.getLocalRepository(testRobo + "*," + sampleRobo);
		eng.runBattle(new BattleSpecification(numRounds, new BattlefieldSpecification(), robots), true);
		//eng.setVisible(true);
		return reporter.result[0].getBulletDamage()/numRounds;
		//return ((double)NeuroTargetingBot.numHits)/numRounds;
	}

	@Override
	public double[] fitness(Population pop) {
		// TODO Auto-generated method stub
		return null;
	}

}
