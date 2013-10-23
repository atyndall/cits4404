package neuroFuzzy;

import java.util.List;

public class GaussianActivation implements ActivationFunction {

	double mean;
	double stddev;
	
	public GaussianActivation(double mean, double stddev) {
		this.mean = mean;
		this.stddev = stddev;
	}
	
	
	@Override
	public double calculate(List<Double> input) {
		double sum = 0;
		for(int i = 0; i < input.size(); i++)
			sum += input.get(i);
		return Math.exp(-(Math.pow(sum-mean,2))/(2*Math.pow(stddev, 2)));
	}

}
