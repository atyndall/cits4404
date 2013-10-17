package NeuroFuzzy;

import java.util.Random;

public class Antecedent implements Neuron {

	private MembershipFunction f;
	
	public Antecedent(MembershipFunction f) {
		Random rand = new Random();
		this.f = f;
	}
	
	public double output(double[] input) {
		return f.getMembership(input[0]);
	}
	
}
