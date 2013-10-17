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
import neuroFuzzy.*;

public class Test {

	final static String genomeFile = "bin/neuro/NeuroBot.data/genome.ser";
	
	public static void main(String[] args) {
		Genome g = new Genome(NeuroBot.numInputs,NeuroBot.numOutputs,3,10,15,0.5);
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
		RobocodeEngine eng = new RobocodeEngine(new File("J:/CITS4404/cits4404/robocode"));
		BattleListener reporter = new BattleListener();
		eng.addBattleListener(reporter);
		RobotSpecification[] robots = eng.getLocalRepository("neuro.NeuroBot*,sample.SittingDuck");
		eng.runBattle(new BattleSpecification(1, new BattlefieldSpecification(), robots), true);
		eng.setVisible(true);
		for(int i = 0; i < reporter.result.length; i++)
			System.out.println(i + " " + reporter.result[i].getScore());
		eng.close();
	}
}
