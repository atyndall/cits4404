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
import neuroEvolution.Genome;
import neuroFuzzy.*;

public class MakeGenome {

	final static String genomeFile = "../robocode/robots/neuro/NeuroBot.data/genome.ser";
	
	public static void main(String[] args) {
		Genome g = new Genome(NeuroBot.numInputs,NeuroBot.numOutputs,1,6,10,0.6);
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
}
