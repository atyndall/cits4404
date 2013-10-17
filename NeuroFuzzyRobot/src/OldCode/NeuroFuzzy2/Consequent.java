package NeuroFuzzy2;

import java.util.List;

public class Consequent {

	double value;
	List<Antecedent> inputs;
	MembershipFunction f;
	
	public Consequent(MembershipFunction f, List<Antecedent> inputs) {
		this.inputs = inputs;
		this.f = f;
	}
	
	public double output() {
		double min = Double.MAX_VALUE;
		for(int i = 0; i < inputs.size(); i++) {
			double tmp = inputs.get(i).value;
			if(tmp < min)
				min = tmp;
		}
		value = f.getMembership(min);
		return value;
	}
	
}
