package NeuroFuzzy;

import java.util.Random;

public class Consequent implements Neuron {


	MembershipFunction f;
	
	public Consequent(MembershipFunction f) {
		this.f = f;

	}
	
	//get crisp value via centroid
	public double output(double[] input) {
		MembershipFunction[] matches = new MembershipFunction[input.length];
		double maxSR = Double.MIN_VALUE;
		double minSL = Double.MAX_VALUE;
		for(int i = 0; i < input.length; i++) {
			matches[i] = new MembershipFunction(f.c,f.sr,f.sl,input[i]);
			maxSR = Math.max(maxSR, f.sr);
			minSL = Math.min(minSL, f.sl);
		}
		int resolution = 100;
		double weightedSum = 0;
		double sum = 0;
		for(int i = 0; i < resolution; i++) {
			double x = minSL + i/(maxSR - minSL);
			double fx = MembershipFunction.or(matches, x);
			weightedSum += x*fx;
			sum += fx;
		}
		return weightedSum/sum;
	}
}
