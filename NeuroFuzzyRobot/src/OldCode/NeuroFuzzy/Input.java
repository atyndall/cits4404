package NeuroFuzzy;

public class Input implements Neuron {

	int index;

	public Input(int index) {
		this.index = index;
	}
	
	public double output(double[] input) {
		return input[index];
	}
	
}
