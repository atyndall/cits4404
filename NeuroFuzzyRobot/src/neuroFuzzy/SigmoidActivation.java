package neuroFuzzy;

import java.util.List;

public class SigmoidActivation implements ActivationFunction {

	double factor;
	
	public SigmoidActivation(double factor) {
		this.factor = factor;
	}
	
	@Override
	public double calculate(List<Double> input) {
		double sum = 0;
		for(Double d : input)
			sum += d;
		return logisticFunction(sum);
	}

	public double logisticFunction(double x) {
		return 1.0/(1+Math.exp(-factor*x));
	}
}
