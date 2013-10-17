package test;

import neuroFuzzy.*;

public class Test {

	public static void main(String[] args) {
		Genome g = new Genome(3,5,4,10,20,0.8);
		NeuralNet nn = new NeuralNet(g);
		double[] input = {1,1,1};
		double[] output = nn.output(input);
		for(Double d : output)
			System.out.println(d);
	}
	
}
