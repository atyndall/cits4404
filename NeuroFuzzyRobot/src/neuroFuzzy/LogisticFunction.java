package neuroFuzzy;

import java.util.List;

public class LogisticFunction implements ActivationFunction {

	@Override
	public double calculate(List<Double> input) {
		double sum = 0;
		for(Double d : input)
			sum += d;
		return logisticFunction(sum);
	}

	public static double logisticFunction(double x) {
		return 1.0/(1+Math.exp(-x));
	}
}
