package NeuroFuzzy;

import java.util.Random;

public class Rule implements Neuron {


	
	public Rule() {
		
	}
	
	//do min
	public double output(double[] input) {
		double min = Double.MAX_VALUE;
		for(int i = 0; i < input.length; i++) {
			double tmp = input[i];
			if(tmp < min)
				min = tmp;
		}
		return min;
	}
	
}
