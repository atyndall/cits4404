package neuroFuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Neuron {

	int id;
	ActivationFunction f;
	List<Neuron> children;
	int numInputs = 0; //this should be updated when you add something as a child
	List<Double> input;
	HashMap<Integer,Double> weights;
	
	public Neuron(int id, ActivationFunction f) {
		this.id = id;
		this.children = new ArrayList<Neuron>();
		this.weights = new HashMap<Integer,Double>();
		this.f = f;
		this.input = new ArrayList<Double>();
	}
	
	public void output() {
		double out = f.calculate(input);
		for(Neuron n : children) {
			//We weight the outputs
			//System.out.println(id + " sending " + out + " X " + weights.get(n.id));
			n.receiveInput(out*weights.get(n.id));
		}
		input = new ArrayList<Double>();
	}
	
	public void receiveInput(double in) {
		//System.out.println(id + " receiving " + in);
		input.add(in);
	}
	
	public void addChild(Neuron child, Double weight) {
		children.add(child);
		weights.put(child.id, weight);
		child.numInputs++;
	}
	
	public String toString() {
		return id + " : " + children.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		Neuron n2 = (Neuron)o;
		return this.id == n2.id;
	}
	
}
