package neuroFuzzy;

import java.util.Comparator;

public class ProcessOrderingComparator implements Comparator<Neuron> {

	@Override
	public int compare(Neuron n1, Neuron n2) {
		return (n1.numInputs - n1.input.size()) - (n2.numInputs - n2.input.size());
	}

}
