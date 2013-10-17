package NeuroFuzzy2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Antecedent {

	List<Input> inputs;
	MembershipFunction f;
	double value;
	
	public Antecedent(MembershipFunction f, Input input) {
		this.inputs = new ArrayList<Input>();
		inputs.add(input);
		this.f = f;
	}
	
	public double output() {
		value = f.getMembership(inputs.get(0).value);
		return value;
	}
	
}
