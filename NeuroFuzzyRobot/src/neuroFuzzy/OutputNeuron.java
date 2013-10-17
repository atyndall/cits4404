package neuroFuzzy;

import java.util.HashMap;
import java.util.List;

public class OutputNeuron extends Neuron {

	public double output;
	
	public OutputNeuron(int id) {
		super(id, null);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void output() {
		this.output = input.get(0);
	}

}
