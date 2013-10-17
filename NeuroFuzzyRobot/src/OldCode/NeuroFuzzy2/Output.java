package NeuroFuzzy2;

import java.util.List;


public class Output {

	double value;
	List<Consequent> inputs;
	
	public Output(double value, List<Consequent> inputs) {
		this.value = value;
		this.inputs = inputs;
	}
	
	public double output() {
		MembershipFunction[] matches = new MembershipFunction[inputs.size()];
		double maxSR = Double.MIN_VALUE;
		double minSL = Double.MAX_VALUE;
		for(int i = 0; i < inputs.size(); i++) {
			Consequent cons = inputs.get(i);
			matches[i] = new MembershipFunction(cons.f.c,cons.f.sr,cons.f.sl,cons.value);
			maxSR = Math.max(maxSR, cons.f.sr);
			minSL = Math.min(minSL, cons.f.sl);
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
