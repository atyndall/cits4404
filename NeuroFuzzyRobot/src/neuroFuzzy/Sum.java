package neuroFuzzy;

import java.util.List;

public class Sum implements ActivationFunction {

	@Override
	public double calculate(List<Double> input) {
		double sum = 0;
		for(int i = 0; i < input.size(); i++)
			sum += input.get(i);
		return sum;
	}

}
