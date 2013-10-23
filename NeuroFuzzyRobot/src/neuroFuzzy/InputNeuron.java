package neuroFuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputNeuron extends Neuron {

	
	public InputNeuron(int id) {
		super(id,new Sum());
	}

}
