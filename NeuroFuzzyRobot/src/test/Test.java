package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;


import neuro.*;
import neuroEvolution.BattleListener;
import neuroEvolution.Genome;
import neuroEvolution.RoboFitness;
import neuroFuzzy.*;

public class Test {

	
	
	public static void main(String[] args) {
		Genome g = new Genome(NeuroBot.numInputs,NeuroBot.numOutputs,1,6,10,0.75);
		try {
			FileOutputStream fileOut = new FileOutputStream(RoboFitness.genomeFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(g);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RobocodeEngine eng = new RobocodeEngine(new File("../robocode"));
		BattleListener reporter = new BattleListener();
		eng.addBattleListener(reporter);
		RobotSpecification[] robots = eng.getLocalRepository("neuro.NeuroBot*,sample.SittingDuck");
		eng.runBattle(new BattleSpecification(10, new BattlefieldSpecification(), robots), true);
		for(int i = 0; i < reporter.result.length; i++)
			System.out.println(i + " " + reporter.result[i].getScore());
		eng.close();
	}
}
